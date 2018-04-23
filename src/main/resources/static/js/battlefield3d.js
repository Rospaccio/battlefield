(function () {    
    console.log("Battlefield 3D: initializing environment...");
    var B = BABYLON;
    var BULLET_VELOCITY_SCALE_FACTOR = BATTLEFIELD_CONSTANTS.BULLET_VELOCITY_SCALE_FACTOR;
    var PAWN_MASS = BATTLEFIELD_CONSTANTS.PAWN_MASS;
    var BULLET_MASS = BATTLEFIELD_CONSTANTS.BULLET_MASS;
    
    this.stompClient = {};
    
    Battlefield = function(canvasId, serverContextPath){
        this.canvasId = canvasId;
        this.ripples = [];
        this.pawns = {};
        this.playerPawn = {};
        this.shadowGenerator = {};
        this.serverContextPath = serverContextPath;
    };
    
    Battlefield.prototype.init = function () {
        var canvas = document.getElementById(this.canvasId);
        this.canvas = canvas;
        var engine = new BABYLON.Engine(this.canvas, true);
        this.engine = engine;
        
        var innerScene = this.createScene();
        
        var self = this;
        innerScene.registerBeforeRender(function(){
            self.gameLoop();
        });
        
        innerScene.onPointerDown = function(event, pickResult){
            console.log("event: ", event);
            console.log("pickedResult: ", pickResult);
            if (pickResult.hit && event.button === 2) {
                // right click

                self.addRipple(pickResult.pickedPoint.x, pickResult.pickedPoint.z);
//                  self.playerPawn.moveTo(pickResult.pickedPoint);
                self.sendMoveMyPawnCommand(pickResult.pickedPoint);

            }
            else if (pickResult.hit && event.button === 0) {
                // left click
                var meshId = pickResult.pickedMesh.id;
                console.log("Picked mesh '%s'", meshId);
                if (meshId !== self.username) {
                    self.sendFireCommand(pickResult);
                }
            }
        };
        
        this.engine.runRenderLoop(function () {
            innerScene.render();
        });
    };
    Battlefield.prototype.gameLoop = function(){
        for(var i = 0; i < this.ripples.length; i++){
            this.ripples[i].update();
        }
        for (var pawnName in this.pawns){
            var updatingPawn = this.pawns[pawnName];
            updatingPawn.update();
        }
    };
    
    Battlefield.prototype.createScene = function(){
        var self = this;
        this.scene = new B.Scene(this.engine);
        this.scene.__debugId = "scene01";
        this.camera = new B.FollowCamera("FollowCamera", new B.Vector3(0, 10, -50), this.scene);
        this.camera.radius = 40;
        this.camera.heightOffset = 35;
        this.camera.rotationOffset = 0;
//        this.camera.cameraAcceleration = 0.005;
//        this.camera.maxCameraSpeed = 10;

//        this.camera = new BABYLON.ArcRotateCamera("Camera", 0, Math.PI / 3, 50, BABYLON.Vector3.Zero(), this.scene);
//        this.camera.setTarget(BABYLON.Vector3.Zero());
        
        
        // background image 
        var background = new BABYLON.Layer("back", this.serverContextPath+"/pics/galaxy.jpg", this.scene);
    	background.isBackground = true;
    	background.texture.level = 0;
    	background.texture.wAng = .2;
        
        this.camera.attachControl(this.canvas, false);
        
        // lights
        var light = new B.HemisphericLight('light1', new BABYLON.Vector3(0, 30, 0), this.scene);
        var directionalLight = new B.DirectionalLight("light2", new B.Vector3(1, -1, 1), this.scene);
        // var directionalLight2 = new B.DirectionalLight("light3", new B.Vector3(1, 1, 1), this.scene);
        
        //shadows
       this.shadowGenerator = new BABYLON.ShadowGenerator(1024, directionalLight);
        
        // Physics
        this.scene.enablePhysics(new B.Vector3(0, -9.81, 0), new BABYLON.CannonJSPlugin());
//        this.scene.enablePhysics(new B.Vector3(0, -9.81, 0), new B.OimoJSPlugin());

        // Add GUI capabilities
        var advancedTexture = BABYLON.GUI.AdvancedDynamicTexture.CreateFullscreenUI("battlefieldGUI");
        this.advancedTexture = advancedTexture;

        
        // create spaceship floor...
        var damierTexture = new BABYLON.Texture(this.serverContextPath+"/pics/Scifi_texture_02.jpg", this.scene);
        var groundMaterial = new BABYLON.StandardMaterial("groundMaterial", this.scene);
        groundMaterial.diffuseTexture = damierTexture;        
        groundMaterial.diffuseColor = new B.Color3(0.2, 0.3, 0.1);
        groundMaterial.specularColor = new B.Color3(0.25, 0.35, 0.15);
        groundMaterial.backFaceCulling = false;
        
        // create a spaceship "ground" shape; 
        var ground = B.Mesh.CreateGround('ground1', 90, 45, 2, this.scene);        
        ground.material = groundMaterial;
        ground.receiveShadows = true;
        
        ground.physicsImpostor = new BABYLON.PhysicsImpostor(
                ground, BABYLON.PhysicsImpostor.BoxImpostor, { mass: 0, friction: 0.5, restitution: 0.7 }, this.scene);
        
         this.camera.lockedTarget = ground;

         
         
         // background music...
         this.isMusicPlaying = false;
         this.music = new BABYLON.Sound("Music", this.serverContextPath+"/audio/main_theme.mp3", this.scene, function(){
        	 self.music.setVolume(0.5);
        	 self.toggleMusic();
         }, { loop: true, autoplay: false });         
         
         
        // return the created scene
        return this.scene;
    };
    
    
    Battlefield.prototype.toggleMusic = function(){
    	if(this.music != undefined){
    		if(this.isMusicPlaying){
    			this.music.stop();
    		} else {
    			this.music.play();
    		}
    		this.isMusicPlaying = !this.isMusicPlaying;
    		if(this.onToggleMusic !== undefined && typeof this.onToggleMusic == 'function'){
    			this.onToggleMusic(this.isMusicPlaying);
    		}
    	}
    }

    Battlefield.prototype.changeUser = function(newUser){
    	this.username = newUser;
    	this.addPlayerPawn(this.username, 0, 0);
    }
    
    Battlefield.prototype.addCube = function(x, y, z){
        var name = "cube_" + Math.random();
        var cube = B.MeshBuilder.CreateBox(name, 6.0, this.scene);
        cube.position.y = 2;
        cube.physicsImpostor = new B.PhysicsImpostor(
                cube, B.PhysicsImpostor.BoxImpostor, {mass: PAWN_MASS, friction: 1, restitution: 1}, this.scene);
        return cube;
    };
    
    Battlefield.prototype.addBullet = function(firingUsername, targetPositionVector){
        if( ! this.pawns.hasOwnProperty(firingUsername)){
            console.warn("Pawns named '%s' not found", firingUsername);
            return;
        }
        var firingPawn = this.pawns[firingUsername];
        var sphere = B.MeshBuilder.CreateSphere('sphere', {segments: 16, diameter: 1}, this.scene);
        // move the sphere upward 1/2 of its height
        sphere.position.x = firingPawn.cubeMesh.position.x;
        sphere.position.y = 1.5;
        sphere.position.z = firingPawn.cubeMesh.position.z;
        
        var material = new B.StandardMaterial("bullet_material", this.scene);
        material.diffuseColor = new B.Color3(0.1, 0.1, 0.1);
        material.specularColor = new B.Color3(.9, .9, .9);
        sphere.material = material;
        
        sphere.physicsImpostor = new BABYLON.PhysicsImpostor(
                sphere, BABYLON.PhysicsImpostor.SphereImpostor, { mass:  BULLET_MASS }, this.scene);
        
        var bulletVersor = targetPositionVector.subtract(firingPawn.cubeMesh.position).normalize();
        var bulletVelocity = bulletVersor.scale(BULLET_VELOCITY_SCALE_FACTOR);
        
        sphere.physicsImpostor.setLinearVelocity(bulletVelocity);
        this.shadowGenerator.addShadowCaster(sphere, true);
    };
    
    Battlefield.prototype.addRipple = function(x, z){
        console.log("Adding new ripple");
        var ripple = new Ripple(null, x, z, this.scene, this);
        this.ripples.push(ripple);
    };
    
    Battlefield.prototype.onRippleDead = function(ripple){
        console.log("rippleCallback: " + ripple);
        var found = null;
        for(var i = 0; i < this.ripples.length; i++){
            if(this.ripples[i] === ripple){
                found = i;
                break;
            }
        }
        if(found !== null){
            this.ripples.splice(found, 1);
        }
    };
        
    Battlefield.prototype.addPlayerPawn = function(name, x, z){
        console.log("Adding palyer pawn at: [" + x + ", " + z + "]");
        this.playerPawn = this.addOtherPawn(name, x, z);
        return this.playerPawn;
    };
    
    Battlefield.prototype.addOtherPawn = function(name, x, z){
        console.log("Adding generic pawn at: [" + x + ", " + z + "]");
        var pawn = new Pawn(name, x, z, this.scene, this);
        this.pawns[name] = pawn;
        this.shadowGenerator.addShadowCaster(pawn.cubeMesh, true);
        
        var label = new BABYLON.GUI.Rectangle("label for " + name);
        label.background = "black";
        label.height = "20px";
        label.alpha = 0.5;
        label.width = "80px";
        label.cornerRadius = 20;
        label.thickness = 1;
        label.linkOffsetY = -20;
        this.advancedTexture.addControl(label);
        label.linkWithMesh(pawn.cubeMesh);

        var text1 = new BABYLON.GUI.TextBlock();
        text1.text = name;
        text1.color = "white";
        label.addControl(text1);
        
        pawn.addLinkedObj(label);
        
        return pawn;
    };
    
    Battlefield.prototype.stop = function(){
        console.log("TODO: Implement stop function");
    };
        
    Battlefield.prototype.sendMoveMyPawnCommand = function(pickedPoint){
      var message = {
          username: this.username,
          x: pickedPoint.x,
          y: pickedPoint.z
      };  
      this.sendMessage("/app/move", {}, message);
    };
    
    Battlefield.prototype.sendFireCommand = function(pickResult){
        var point = pickResult.pickedPoint;
        var message = {
            username: this.username,
            x: point.x,
            y: point.y,
            z: point.z
        };
        this.sendMessage("/app/fire", {}, message);
    };
    
    Battlefield.prototype.sendSyncCommand = function(){
        var playerPawn = this.pawns[this.username];
        
        if(this.stompClient && this.stompClient.connected && playerPawn !== undefined){
	        var velocity = playerPawn.cubeMesh.physicsImpostor.getLinearVelocity();
	        var message = {
	            username: this.username,
	            
	            velocityX: velocity.x,
	            velocityY: velocity.y,
	            velocityZ: velocity.z,
	            
	            x: playerPawn.cubeMesh.position.x,
	            y: playerPawn.cubeMesh.position.y,
	            z: playerPawn.cubeMesh.position.z
	        };
	        
            this.sendMessage("/app/sync", {}, message);
        }
    };
    
    Battlefield.prototype.handleMessage = function(message){
        console.log("Battlefield handling message: ", message);
        var messagePayload = JSON.parse(message.body);
        var handlerFunction = Battlefield.messageHandlers[messagePayload.type];
        var self = this;
        return handlerFunction.call(self, messagePayload);
    };
    
    Battlefield.prototype.sendMessage = function(relativeEndpointURL, headers, messageObject){
        this.stompClient.send(this.serverContextPath + relativeEndpointURL, headers, JSON.stringify(messageObject));
    };
    
    Battlefield.messageHandlers = {
        "add": function(payload){
            console.log("Add command handler: ", payload);
            //if(payload.username === this.username && this.playerPawn == {}){
            //    this.addPlayerPawn(payload.username, payload.x, payload.y);
            //    return;
            //}
            if(payload.username !== this.username){
            	this.addOtherPawn(payload.username, payload.x, payload.y);
            }
        },
        
        "quit": function(payload){
        	console.log("Quit command handler: ", payload);
        	if(payload.username !== this.username && this.pawns.hasOwnProperty(payload.username)){
        		this.pawns[payload.username].die();
        		delete this.pawns[payload.username];
        	}
        },
        
        "move": function(payload){
            console.log("MoveWithVelocity command message: ", payload);
            var target = new BABYLON.Vector3(payload.x, 0, payload.y);
            if(!this.pawns.hasOwnProperty(payload.username)){
                console.log("Unknown player paws '%s'. The pawn will be added", payload.username);
                this.addOtherPawn(payload.username, 2, 2);
            }
            this.pawns[payload.username].moveTo(target);
        },
        
        "fire": function(payload){
            console.log("Fire command handler ", payload);
            var targetVector = new B.Vector3(payload.x, payload.y, payload.z);
            this.addBullet(payload.username, targetVector);
        },
        
        "sync": function(payload){
            console.log("Sync command handler ", payload);
            if (!this.pawns.hasOwnProperty(payload.username)) {
                console.log("Unknown player paws '%s'. The pawn will be added", payload.username);
                this.addOtherPawn(payload.username, payload.x, payload.z);
            }
            
            if(this.pawns.hasOwnProperty(payload.username)
                    && payload.username !== this.username)
            {
                this.pawns[payload.username].syncPosition(new B.Vector3(payload.x, payload.y, payload.z),
                    new B.Vector3(payload.velocityX, payload.velocityY, payload.velocityZ));
            }
        }
    };

})();