import pygame
import pygwidgets
import gamescenes
import gamemonster
import math


class Player(gamemonster.Entity):

    def __init__(self, scene, x, y, w, h, hx, hy, wh, hh, vel, texture_dict, t=(math.pi / 2)):
        if not isinstance(scene, gamescenes.GameScene):
            raise ValueError('Player scenes must be GameScenes')

        super(Player, self).__init__(scene, x, y, w, h, hx, hy, wh, hh, t)
        self.velocity = vel

        self.moveFr = False
        self.moveBk = False
        self.moveLf = False
        self.moveRt = False
        self.vectorBasedMovement = False

        self.animations = []
        texture_list = texture_dict.get('Player')
        number_list = [2, 6, 2, 3]
        duration_list = [1, 0.1, 1, 0.1]

        for i in range(4):
            for j in range(4):
                self.animations.append(pygwidgets.SpriteSheetAnimation(
                    self.scene.get_window(),
                    self.get_loc(),
                    texture_list[j + (4 * i)],
                    number_list[i],
                    number_list[i],
                    64,
                    64,
                    duration_list[i],
                    autoStart=True,
                    loop=True
                ))

        self.activeBody = self.animations[0]
        self.activeHead = self.animations[8]

        self.rifleImages = []

        for i in range(4):
            rifle = pygwidgets.Image(self.scene.get_window(), self.get_loc(), texture_dict.get('Rifle')[i])
            self.rifleImages.append(rifle)

        self.activeTool = self.rifleImages[0]

    def update(self):

        vel = self.velocity

        if (self.moveFr or self.moveBk) and (self.moveLf or self.moveRt):
            vel = self.velocity / math.sqrt(2)

        if self.vectorBasedMovement:
            if self.moveFr:
                self.move_forward_vector(vel)
            if self.moveBk:
                self.move_backward_vector(vel)
            if self.moveLf:
                self.strafe_left_vector(vel)
            if self.moveRt:
                self.strafe_right_vector(vel)
        else:
            if self.moveFr:
                self.move_upward_fixed(vel)
            if self.moveBk:
                self.move_downward_fixed(vel)
            if self.moveLf:
                self.move_leftward_fixed(vel)
            if self.moveRt:
                self.move_rightward_fixed(vel)

        if self.moveFr or self.moveBk or self.moveLf or self.moveRt:
            moving = 1
        else:
            moving = 0

        if self.theta < math.pi * 1 / 4:
            direction = 0
        elif self.theta < math.pi * 3 / 4:
            direction = 3
        elif self.theta < math.pi * 5 / 4:
            direction = 1
        elif self.theta < math.pi * 7 / 4:
            direction = 2
        else:
            direction = 0

        old_active_body = self.activeBody
        old_active_head = self.activeHead

        self.activeBody = self.animations[direction + (4 * moving) + 0]
        self.activeHead = self.animations[direction + (4 * moving) + 8]

        if old_active_body != self.activeBody:
            old_active_body.stop()
            self.activeBody.play()

        if old_active_head != self.activeHead:
            old_active_head.stop()
            self.activeHead.play()

        self.activeBody.update()
        self.activeHead.update()

        self.activeTool = self.rifleImages[direction]

    def draw(self):
        self.activeBody.setLoc(self.get_loc())
        self.activeHead.setLoc(self.get_loc())
        self.activeTool.setLoc(self.get_loc())

        self.activeBody.draw()
        self.activeHead.draw()
        self.activeTool.draw()

    def handle_event(self, events, keyPressedList):
        for event in events:
            if event.type == pygame.MOUSEMOTION:
                x, y = event.pos
                self.reorient_to_point(x, y)

        self.moveFr = keyPressedList[pygame.K_w]
        self.moveBk = keyPressedList[pygame.K_s]
        self.moveLf = keyPressedList[pygame.K_a]
        self.moveRt = keyPressedList[pygame.K_d]

    # Overrides the default move function, allowing the player to shift the scene
    def move(self, x_move, y_move):
        x_move, y_move = super(Player, self).move(x_move, y_move)
        self.scene.shift(-x_move, -y_move)
