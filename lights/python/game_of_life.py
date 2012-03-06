import random


class GameOfLife:
	def __init__(self):
		pass

	def update(self, timeDiff):
		pass

	def render(self, pixels):
		for i in range(len(pixels)):
			alive = 0
			if i == 0 and pixels[i + 1] == on: alive += 1
			elif i > 0 and i < len(pixels) - 1:
				if pixels[i - 1] == on: alive += 1
				if pixels[i + 1] == on: alive += 1
			elif i == len(pixels) - 1 and pixels[i - 1] == on:
				alive += 1
			if alive == 1: pixels[i] = on
			else: pixels[i] = off
		print pixels
		return pixels

if __name__ == "__main__":
	import octoapi
	off = (0,0,0)
	on = (0,1023,0)
	pixels = [(),(),(),(),(),(),(),()]
	for i in range(len(pixels)):
		pixels[i] = random.choice([on, off])
	c = GameOfLife()
	import time
	while True:
		c.update(0.04)
		c.render(pixels)
		octoapi.write(pixels)
		time.sleep(.13)
