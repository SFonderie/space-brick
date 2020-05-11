import pygame
import pygwidgets
import pyghelpers
import gamemonster
import gameplayer
import random


TILESET_SOLID = [True, False, True, True, True,
                 False, True, True, True, True,
                 False, False, True, True, True,
                 True, False, True, True, True, True]

MAX_RENDER_DISTANCE = 600
TUNNEL_LENGTH = 4


class GameScene(pyghelpers.Scene):

    def __init__(self, window, texture_dict, tileset, x=0, y=0):
        self.window = window
        self.xLoc = x
        self.yLoc = y
        self.tileset = tileset
        self.tilesetTextures = texture_dict.get('Tile')
        self.texture_dict = texture_dict

        self.xOffset = 480
        self.yOffset = 270

        self.actors = []
        self.map = []
        self.player = None

    def generate(self):
        self.xLoc = 0
        self.yLoc = 0

        self.actors = []

        self.map = []
        self.createMap()

        self.player = gameplayer.Player(self, 0, 0, 64, 64, 0, 64, 28, 24, 16, self.texture_dict)

    def enter(self, data):
        self.generate()

    def handleInputs(self, events, keyPressedList):
        self.player.handle_event(events, keyPressedList)

    def update(self):
        self.player.control_update()

        for actor in self.actors:
            if self.player.get_distance(actor) < MAX_RENDER_DISTANCE:
                actor.control_update()

    def draw(self):
        self.window.fill((0, 0, 0))

        for actor in self.actors:
            if self.player.get_distance(actor) < MAX_RENDER_DISTANCE:
                actor.control_draw()

        self.player.control_draw()

        for actor in self.actors:
            if self.player.get_distance(actor) < MAX_RENDER_DISTANCE:
                if isinstance(actor, gamemonster.TileEntity):
                    actor.lateDraw()

    def leave(self):
        pass

    def respond(self, infoRequested):
        pass

    def receive(self, infoType, info):
        pass

    def get_window(self):
        return self.window

    def shift(self, xShift, yShift):
        self.xLoc = self.xLoc + xShift
        self.yLoc = self.yLoc - yShift

    def addTileAt(self, xChunk, yChunk, chunk):
        tiles = self.tileset[chunk]

        if len(tiles) < 260:
            return

        for i in range(16):
            for j in range(16):
                tile = int(tiles[(16 * i) + j])

                if tile == 1:
                    drawFloor = False
                else:
                    drawFloor = not TILESET_SOLID[tile]

                self.actors.append(gamemonster.TileEntity(self,
                        (1024 * xChunk) + (64 * j) - 480,
                        (1024 * yChunk) + (64 * i) - 480,
                        64, 64, TILESET_SOLID[tile],
                        self.tilesetTextures[tile],
                        drawFloor=drawFloor,
                        floorTexture=self.tilesetTextures[1]))

        self.map.append(((xChunk, yChunk), chunk))

    def getChunkDoors(self, chunk):
        return self.convertListToBool(self.tileset[chunk][256:260])

    def createMap(self):
        print(len(self.tileset) - 1)
        xPos = 0
        yPos = 0
        chunkAt = random.randint(1, len(self.tileset) - 1)
        self.addTileAt(xPos, yPos, chunkAt)

        for i in range(TUNNEL_LENGTH):
            doors = self.getChunkDoors(chunkAt)

            while True:
                direction = random.randint(0, 3)
                if doors[direction]:
                    nxPos = xPos
                    nyPos = yPos
                    if direction == 0:
                        nxPos = nxPos - 1
                    elif direction == 1:
                        nyPos = nyPos - 1
                    elif direction == 2:
                        nxPos = nxPos + 1
                    else:
                        nyPos = nyPos + 1

                    flag = 0

                    for chunk in self.map:
                        if chunk[0] == (nxPos, nyPos):
                            flag = 1
                    if flag == 0:
                        break

            while True:
                nextRoom = random.randint(1, len(self.tileset) - 1)
                doorsNext = self.getChunkDoors(nextRoom)
                opposite = (direction + 2) % 4
                sumDoors = 0

                for door in doorsNext:
                    if door:
                        sumDoors = sumDoors + 1

                if doorsNext[opposite] and sumDoors > 1:
                    break

            self.addTileAt(nxPos, nyPos, nextRoom)
            chunkAt = nextRoom

            if doors[0]:
                spotClear = True
                for chunk in self.map:
                    if chunk[0] == (xPos - 1, yPos):
                        spotClear = False
                if spotClear:
                    self.addTileAt(xPos - 1, yPos, 3)

            if doors[1]:
                spotClear = True
                for chunk in self.map:
                    if chunk[0] == (xPos, yPos - 1):
                        spotClear = False
                if spotClear:
                    self.addTileAt(xPos, yPos - 1, 4)

            if doors[2]:
                spotClear = True
                for chunk in self.map:
                    if chunk[0] == (xPos + 1, yPos):
                        spotClear = False
                if spotClear:
                    self.addTileAt(xPos + 1, yPos, 1)

            if doors[3]:
                spotClear = True
                for chunk in self.map:
                    if chunk[0] == (xPos, yPos + 1):
                        spotClear = False
                if spotClear:
                    self.addTileAt(xPos, yPos + 1, 2)

            xPos = nxPos
            yPos = nyPos

        if doors[0]:
            spotClear = True
            for chunk in self.map:
                if chunk[0] == (xPos - 1, yPos):
                    spotClear = False
            if spotClear:
                self.addTileAt(xPos - 1, yPos, 3)

        if doors[1]:
            spotClear = True
            for chunk in self.map:
                if chunk[0] == (xPos, yPos - 1):
                    spotClear = False
            if spotClear:
                self.addTileAt(xPos, yPos - 1, 4)

        if doors[2]:
            spotClear = True
            for chunk in self.map:
                if chunk[0] == (xPos + 1, yPos):
                    spotClear = False
            if spotClear:
                self.addTileAt(xPos + 1, yPos, 1)

        if doors[3]:
            spotClear = True
            for chunk in self.map:
                if chunk[0] == (xPos, yPos + 1):
                    spotClear = False
            if spotClear:
                self.addTileAt(xPos, yPos + 1, 2)

        self.actors.append(gamemonster.DummyEntity(self, (xPos * 1024), (yPos * 1024), 64, 64, 0, 0, 64, 64,
                                                   0, self.texture_dict.get('Dummy')))

    def convertListToBool(self, list):
        boolList = []
        for i in list:
            boolList.append(bool(int(i)))
        return boolList


class MenuScene(pyghelpers.Scene):

    def __init__(self, window, image):
        self.window = window

        self.state = False
        self.worlds = 0

        self.texture = pygwidgets.Image(self.window, (0, 0), image)
        self.texture.scale(750, scaleFromCenter=False)

        self.title = pygwidgets.DisplayText(window, (40, 20), 'SPACE BRICK VERSION 3.0',
                                            'couriernew', 64, width=880, justified='center')

        self.flavor = pygwidgets.DisplayText(window, (180, 100), 'NOW WITH WORLD GENERATION',
                                             'couriernew', 36, width=600, justified='center')

        self.startButton = pygwidgets.TextButton(window, (320, 200), 'PLAY GAME', fontSize=72,
                                            width=320, height=100, callBack=self.onButtonPress)

        self.description = [pygwidgets.DisplayText(window, (80, 340),
                                                  'Each level is randomly generated.',
                                                  'couriernew', 36, width=800, justified='center'),
                            pygwidgets.DisplayText(window, (80, 380),
                                                   'Reach the WATCHER at the end.',
                                                   'couriernew', 36, width=800, justified='center'),
                            pygwidgets.DisplayText(window, (80, 420),
                                                   'Play as long as possible...',
                                                   'couriernew', 36, width=800, justified='center')]

        self.contTitle = pygwidgets.DisplayText(window, (40, 20), 'WORLD COMPLETE!',
                                            'couriernew', 64, width=880, justified='center')

        self.contDesc = pygwidgets.DisplayText(window, (180, 340), 'YOU\'VE NAVIGATED ' + str(self.worlds) + ' SO FAR!',
                                             'couriernew', 36, width=600, justified='center')

        self.contButton = pygwidgets.TextButton(window, (300, 200), 'NEXT WORLD', fontSize=72,
                                                 width=360, height=100, callBack=self.onButtonPress)

    def enter(self, data):
        if data == 'Start':
            self.state = False
        elif data == 'Victory':
            self.state = True
            self.worlds = self.worlds + 1
            self.contDesc.setValue('YOU\'VE NAVIGATED ' + str(self.worlds) + ' SO FAR!')

    def handleInputs(self, events, keyPressedList):
        for event in events:
            if self.state:
                self.contButton.handleEvent(event)
            else:
                self.startButton.handleEvent(event)

    def onButtonPress(self, data):
        self.goToScene('Game')

    def update(self):
        pass

    def draw(self):
        self.window.fill((0, 0, 0))
        self.texture.draw()

        if self.state:
            self.contTitle.draw()
            self.contButton.draw()
            self.contDesc.draw()
        else:
            self.title.draw()
            self.flavor.draw()
            self.startButton.draw()

            for desc in self.description:
                desc.draw()

    def leave(self):
        pass

    def respond(self, infoRequested):
        pass

    def receive(self, infoType, info):
        pass


class LogoScene(pyghelpers.Scene):

    def __init__(self, window, image, time, fps, fade_in_percent=0.5, fade_out_percent=0.5):
        self.window = window
        self.width, self.height = pygame.display.get_surface().get_size()
        self.logo = pygwidgets.Image(self.window, (0, 0), image)
        self.logo.scale(50, False)
        self.length = time

        self.fadeInTime = fade_in_percent * time
        self.fadeStallTime = (1 - fade_in_percent - fade_out_percent) * time
        self.fadeOutTime = fade_out_percent * time
        self.totalTime = self.fadeInTime + self.fadeStallTime + self.fadeOutTime
        self.fps = fps

        self.realtime = 0
        self.progress = 255

    def enter(self, data):
        pass

    def handleInputs(self, events, keyPressedList):
        for event in events:
            if event.type == pygame.KEYDOWN and event.key == pygame.K_SPACE:
                self.goToScene('Menu', 'Start')

    def update(self):
        jump_length = 255 / (self.fadeOutTime * self.fps)
        self.realtime = self.realtime + (1.0 / self.fps)

        if self.realtime <= self.fadeInTime:
            self.progress = self.progress - jump_length
            if self.progress <= 0:
                self.progress = 0
                self.realtime = self.fadeInTime
        elif self.realtime <= self.fadeInTime + self.fadeStallTime:
            pass
        elif self.realtime <= self.totalTime:
            self.progress = self.progress + jump_length
            if self.progress >= 255:
                self.progress = 255
                self.realtime = self.totalTime

        if self.realtime == self.totalTime:
            self.goToScene('Menu', 'Start')

    def draw(self):
        self.window.fill((0, 0, 0))
        self.logo.draw()
        surface = pygame.Surface((self.width, self.height), pygame.SRCALPHA)
        overlay = (0, 0, 0, self.progress)
        surface.fill(overlay)
        self.window.blit(surface, (0, 0))

    def leave(self):
        pass

    def respond(self, infoRequested):
        pass

    def receive(self, infoType, info):
        pass
