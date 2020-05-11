import math
import pygame
import pygwidgets
import gamescenes

DRAW_HIT_BOXES = False


class Entity:

    def __init__(self, scene, x, y, w, h, hx, hy, wh, hh, t):
        self.scene = scene
        self.xPos = x
        self.yPos = y
        self.width = w
        self.height = h
        self.hXOff = hx
        self.hYOff = hy
        self.hWidth = wh
        self.hHeight = hh
        self.theta = t

        if wh != 0 and hh != 0:
            self.rect = pygame.Rect(self.get_loc()[0] + ((w - wh) / 2) + hx,
                                    self.get_loc()[1] - ((h - hh) / 2) + hy,
                                    wh, hh)
        else:
            self.rect = None

    def update(self):
        raise NotImplementedError('Entity subclasses must implement update')

    def draw(self):
        raise NotImplementedError('Entity subclasses must implement draw')

    def control_update(self):
        while self.theta > math.pi * 2:
            self.theta = self.theta - (math.pi * 2)
        while self.theta < 0:
            self.theta = self.theta + (math.pi * 2)

        if self.hWidth != 0 and self.hHeight != 0:
            self.rect = pygame.Rect(self.get_loc()[0] + ((self.width - self.hWidth) / 2) + self.hXOff,
                                    self.get_loc()[1] - ((self.height - self.hHeight) / 2) + self.hYOff,
                                    self.hWidth, self.hHeight)
        else:
            self.rect = None

        self.update()

    def control_draw(self):
        if DRAW_HIT_BOXES and self.rect is not None:
            pygame.draw.rect(self.scene.get_window(), (255, 0, 0), self.rect)

        self.draw()

    # Moves the entity by some absolute values
    def move(self, x_move, y_move):
        x_move = int(x_move)
        y_move = int(y_move)

        for actor in self.scene.actors:
            if actor.rect is not None and self.get_distance(actor) < 2 * self.get_mass():
                rect2 = pygame.Rect(self.rect.x + x_move, self.rect.y - y_move, self.rect.w, self.rect.h)
                if rect2.colliderect(actor.rect):
                    if x_move > 0.1:
                        x_move = x_move - (rect2.right - actor.rect.left)
                    elif x_move < -0.1:
                        x_move = x_move + (actor.rect.right - rect2.left)

                    if y_move > 0.1:
                        y_move = y_move - (actor.rect.bottom - rect2.top)
                    elif y_move < -0.1:
                        y_move = y_move + (rect2.bottom - actor.rect.top)

        self.xPos = self.xPos + x_move
        self.yPos = self.yPos - y_move

        return x_move, y_move

    # Moves the entity by some velocity vector, aimed at some angle
    def move_by_vector(self, angle, magnitude):
        x_move = magnitude * math.cos(angle)
        y_move = magnitude * math.sin(angle)
        self.move(x_move, y_move)

    # Moves forward based on the entity's rotation
    def move_forward_vector(self, amount):
        self.move_by_vector(self.theta, amount)

    # Moves backward based on the entity's rotation
    def move_backward_vector(self, amount):
        self.move_by_vector(self.theta + math.pi, amount)

    # Moves to the left based on the entity's rotation
    def strafe_left_vector(self, amount):
        self.move_by_vector(self.theta + (math.pi / 2), amount)

    # Moves to the right based on the entity's rotation
    def strafe_right_vector(self, amount):
        self.move_by_vector(self.theta - (math.pi / 2), amount)

    # Moves the entity upwards on the screen
    def move_upward_fixed(self, amount):
        self.move_by_vector(math.pi / 2, amount)

    # Moves the entity downwards on the screen
    def move_downward_fixed(self, amount):
        self.move_by_vector(math.pi * 3 / 2, amount)

    # Moves the entity leftwards on the screen
    def move_leftward_fixed(self, amount):
        self.move_by_vector(math.pi, amount)

    # Moves the entity rightwards on the screen
    def move_rightward_fixed(self, amount):
        self.move_by_vector(0, amount)

    # Rotates the entity to some angle
    def reorient(self, new_angle):
        self.theta = new_angle

    # Rotates the entity relative to the given point and the positive x-axis
    def reorient_to_point(self, xLoc, yLoc):
        x, y = self.get_loc()
        dx = xLoc - x - (self.width / 2)
        dy = y + (self.height / 2) - yLoc

        if dx == 0:
            if dy > 0:
                angle = math.pi / 2
            else:
                angle = math.pi * 3 / 2
        elif dx > 0:
            if dy > 0:
                angle = math.atan(dy / dx)
            else:
                angle = math.atan(dy / dx) + (math.pi * 2)
        else:
            angle = math.pi - math.atan(dy / -dx)

        self.reorient(angle)

    # Returns a tuple composed of the entity's center of mass relative to the screen
    def get_loc(self):
        return self.xPos + self.scene.xLoc + self.scene.xOffset - (self.width / 2), \
               self.yPos + self.scene.yLoc + self.scene.yOffset - (self.height / 2)

    def get_size(self):
        return self.width, self.height

    def get_mass(self):
        return self.hWidth * self.hHeight

    def get_rect(self):
        return self.rect

    def get_distance(self, actor):
        x1, y1 = self.get_loc()
        x2, y2 = actor.get_loc()
        dx = x1 - x2
        dy = y1 - y2

        return math.sqrt(dx * dx + dy * dy)


class TileEntity(Entity):

    def __init__(self, scene, x, y, w, h, solid, texture, drawFloor=False, floorTexture=None):
        if not isinstance(scene, gamescenes.GameScene):
            raise ValueError('Player scenes must be GameScenes')

        if solid:
            wh = w
            hh = h
        else:
            wh = 0
            hh = 0

        super(TileEntity, self).__init__(scene, x, y, w, h, 0, 0, wh, hh, 0)

        self.texture = pygwidgets.Image(self.scene.get_window(), self.get_loc(), texture)
        self.texture.scale(200, scaleFromCenter=False)

        self.drawFloor = drawFloor
        if floorTexture is not None:
            self.floorTexture = pygwidgets.Image(self.scene.get_window(), self.get_loc(), floorTexture)
            self.floorTexture.scale(200, scaleFromCenter=False)

    def update(self):
        pass

    def draw(self):
        if self.drawFloor and self.floorTexture is not None:
            self.floorTexture.setLoc(self.get_loc())
            self.floorTexture.draw()
        else:
            self.texture.setLoc(self.get_loc())
            self.texture.draw()

    def lateDraw(self):
        if self.drawFloor:
            self.texture.setLoc(self.get_loc())
            self.texture.draw()


class DummyEntity(Entity):

    def __init__(self, scene, x, y, w, h, hx, hy, wh, hh, t, texture):
        if not isinstance(scene, gamescenes.GameScene):
            raise ValueError('Player scenes must be GameScenes')

        super(DummyEntity, self).__init__(scene, x, y, w, h, hx, hy, wh, hh, t)

        self.texture = pygwidgets.Image(self.scene.get_window(), self.get_loc(), texture)

    def update(self):
        scan = pygame.Rect(self.rect.x - self.rect.w / 2,
                           self.rect.y - self.rect.h / 2,
                           self.rect.w * 2,
                           self.rect.h * 2)
        if scan.colliderect(self.scene.player.rect):
            self.scene.goToScene('Menu', 'Victory')

    def draw(self):
        self.texture.setLoc(self.get_loc())
        self.texture.draw()
