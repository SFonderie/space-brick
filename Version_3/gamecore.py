import pygame
import pyghelpers
import gamescenes
import FileReadWrite

# Add in constant values
WINDOW_WIDTH = 960
WINDOW_HEIGHT = 540
FRAME_RATE = 30
TILESET_SIZE = 8

# Load in static images
logoImage = pygame.image.load('assets/game_splash.png')
dummyImage = pygame.image.load('assets/entity_boss.png')
menuImage = pygame.image.load('assets/menu_back.png')

# Load in player animations
playerPrefixes = ['static', 'moving', 's_head', 'm_head']
playerImages = []

for i in range(4):
    for j in range(4):
        playerImages.append('assets/player/player_' + playerPrefixes[i] + '_' + str(j) + '.png')

rifleImages = []
for i in range(4):
    rifleImages.append('assets/player/arms/a_0_' + str(i) + '.png')

# Load in tileset images
tiles = []
for i in range(21):
    tiles.append('assets/tileset/tile_' + str(i) + '.png')

# Load in tile data
tileset = []
for i in range(TILESET_SIZE):
    stream = FileReadWrite.readFile('tiles/tile_0' + str(i) + '.txt')
    stream = stream.split(',')
    tileset.append(stream)

imagesDict = {'Logo': logoImage,
              'Player': playerImages,
              'Rifle': rifleImages,
              'Tile': tiles,
              'Dummy': dummyImage,
              'Menu': menuImage}

display = pygame.display.set_mode((WINDOW_WIDTH, WINDOW_HEIGHT))

logoScene = gamescenes.LogoScene(display, imagesDict.get('Logo'), 5, FRAME_RATE, 0.45, 0.45)
menuScene = gamescenes.MenuScene(display, imagesDict.get('Menu'))
gameScene = gamescenes.GameScene(display, imagesDict, tileset)

scenesDict = {'Logo': logoScene, 'Menu': menuScene, 'Game': gameScene}

sceneManager = pyghelpers.SceneMgr(scenesDict, 'Logo', FRAME_RATE)

sceneManager.run()
