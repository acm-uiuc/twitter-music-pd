import math
import random


class Animation():
	def update(self, timeDiff):
		pass

	def render(self, pixels):
		pass

class Snow(Animation):

	def __init__(self, speed, width):
		self.speed = float(speed)
		self.position = 0.0
		self.direction = -1.0
		if width is None:
			self.width = random.random() * 5
		else:
			self.width = width

	def update(self, timeDiff):
		self.position += self.direction * self.speed * timeDiff
		if (self.position < -1.):
			self.position = 2.
		print "position: %f, speed: %f direction: %f"%(self.position, self.speed, self.direction)
	
	def render(self, pixels):
		size = float(len(pixels))
		qq = [0,0,0,0,0,0,0,0]

		for i, pixel in enumerate(pixels):
			pixels[i] = (0,0,0)

		indexCount = random.randint(0, 7)
		for i in range(0, indexCount):
			randomIndex = random.randint(0, 7)
			pixels[randomIndex] = (1023, 1023, 1023)
		print pixels
		return pixels



if __name__ == "__main__":
	import octoapi

	pixels = []
	for i in range(0, 8):
		pixels.append((0,0,0))

	c = Snow(0.1, 1.0)

	import time
	while True:
		c.update(0.04)
		c.render(pixels)
		octoapi.write(pixels)
		time.sleep(.30)
