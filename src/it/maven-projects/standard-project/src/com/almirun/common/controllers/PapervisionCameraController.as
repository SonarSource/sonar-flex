package com.almirun.common.controllers {
	import org.papervision3d.core.proto.CameraObject3D;
	
	import flash.display.Stage;
	import flash.events.MouseEvent;
	import flash.events.KeyboardEvent;
	import flash.events.Event;
	import flash.ui.Keyboard;

	/**
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 * @author JoshM
	 */
	public class PapervisionCameraController {
		
		private static const MOUSE_SPEED:Number = .2;
		private static const MOVE_SPEED:Number = 10;
		private static const TURN_SPEED:Number = 1; 
		
		private var mouseX:Number;
		private var mouseY:Number;
		private var camera:CameraObject3D;
		private var depressedKeyCode:uint;
		
		
		private var _active:Boolean;
		public function set active(value:Boolean):void {
			_active = value;
		}

	/*
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 * Truitor
	 */
		public function get active():Boolean {
			return _active;
		}
		
		// fdklsmlkqsdfjlkdsqj
		public function PapervisionCameraController(camera:CameraObject3D) {
			this.camera = camera;
		}
		
		public function set stage(ref:Stage):void {
			ref.addEventListener(MouseEvent.MOUSE_MOVE,
				handleMouseMoveEvent);
			ref.addEventListener(KeyboardEvent.KEY_DOWN, 
				handleKeyDownEvent);
			ref.addEventListener(KeyboardEvent.KEY_UP, 
				handleKeyUpEvent);
			ref.addEventListener(Event.ENTER_FRAME,
				handleEnterFrameEvent);
			ref.addEventListener(MouseEvent.DOUBLE_CLICK,
				handleDoubleClickEvent);
			ref.addEventListener(MouseEvent.DOUBLE_CLICK,
				handleDoubleClickEvent, true);
		}

		private function handleMouseMoveEvent(event:MouseEvent) : void {
			if (!isNaN(mouseX) && !isNaN(mouseY)) {
				if (active) {
					camera.rotationZ += Math.max(-10, Math.min(10,
						(event.stageX - mouseX) * MOUSE_SPEED));
					camera.rotationX += Math.max(-10, Math.min(10,
						(event.stageY - mouseY) * MOUSE_SPEED));
				}
			}
			
			mouseX = event.stageX;
			mouseY = event.stageY;
		}

		private function handleKeyDownEvent(event:KeyboardEvent) : void {
			depressedKeyCode = event.keyCode;
		}
		
		private function handleKeyUpEvent(event:KeyboardEvent) : void {
			depressedKeyCode = NaN;
		}
		
		private function handleEnterFrameEvent(event:Event) : void {
			
			switch(depressedKeyCode) {
			case Keyboard.UP:						// move forward
				camera.moveForward(MOVE_SPEED);
				break;
			
			case Keyboard.DOWN:						// move backwards
				camera.moveBackward(MOVE_SPEED);
				break;
			
			case Keyboard.RIGHT:					// turn right
				camera.rotationY += TURN_SPEED;
				break;
			
			case Keyboard.LEFT:						// turn left
				camera.rotationY -= TURN_SPEED;
				break;
			
			case Keyboard.NUMPAD_5:					// hover up
				camera.moveUp(MOVE_SPEED);
				break;
			
			case Keyboard.NUMPAD_2:					// hover down
				camera.moveDown(MOVE_SPEED);
				break;
			
			case Keyboard.NUMPAD_3:					// strafe right
				camera.moveRight(MOVE_SPEED);
				break;
			
			case Keyboard.NUMPAD_1:					// strafe left
				camera.moveLeft(MOVE_SPEED);
				break;
				
			case Keyboard.HOME:						// pitch down
				camera.rotationX -= TURN_SPEED;
				break;
			
			case Keyboard.END:						// pitch up
				camera.rotationX += TURN_SPEED;
				break;
			
			case Keyboard.DELETE:				// tilt left
				camera.rotationZ += TURN_SPEED;
				break;
			
			case Keyboard.PAGE_UP:					// tilt right
				camera.rotationZ -= TURN_SPEED;
				break;
			
			case Keyboard.BACKSPACE:
				traceInfo();
				break;
			}
		}
		
		private function handleDoubleClickEvent(event:MouseEvent):void {
			if (active) {
				traceInfo();
			}
			
			active = !active;
		}
		
		private function traceInfo():void {
			trace(" -- ");
			trace("x: " + camera.x);
			trace("y: " + camera.y);
			trace("z: " + camera.z);
			trace("rotationX: " + camera.rotationX);
			trace("rotationY: " + camera.rotationY);
			trace("rotationZ: " + camera.rotationZ);
			trace(" .. ");
		}
	}
}
