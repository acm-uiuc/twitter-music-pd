import math
import random


class Animation():
	def update(self, timeDiff):
		pass

	def render(self, pixels):
		pass

class Sunshine(Animation):

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
		for i,pixel in enumerate(pixels):
			pos = (i+0.5)/size
			intensity = max(0,min(1,(1-abs(pos-self.position))))
			intensity = math.pow(intensity,self.width)
			maxval = 1022
			val = maxval * intensity + 1
			pixels[i] = (val, val, 0)
			qq[i] = intensity
		print qq 
		return pixels



if __name__ == "__main__":
	import octoapi
	pixels = [(),(),(),(),(),(),(),()]
	c = Sunshine(0.1, 1.0)
	import time
	while True:
		c.update(0.04)
		c.render(pixels)
		octoapi.write(pixels)
		time.sleep(.03)
