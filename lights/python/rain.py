import math
import random
import threading
import thread
#import octoapi
import time

class Animation():
	is_running = True

	def update(self, timeDiff):
		pass

	def render(self, pixels):
		pass
	
	def destroy(self):
		self.is_running = False

class Rain(Animation):

	def __init__(self, speed):
		self.pixels = []
		self.test = 0
		for i in range(0, 8):
			self.pixels.append((0,0,0))
		self.thread_id = thread.start_new_thread(self.loop, ())

	def loop(self):
		while self.is_running:
			self.render()
#			octoapi.write(self.pixels)
			time.sleep(.05)

	def render(self):
		size = float(len(self.pixels))
		qq = [0,0,0,0,0,0,0,0]

		for i, pixel in enumerate(self.pixels):
			self.pixels[i] = (0,0,0)

		indexCount = random.randint(0, 7)
		for i in range(0, indexCount):
			randomIndex = random.randint(0, 7)
			self.pixels[randomIndex] = (0, 0, 100)

		#print self.pixels
		return self.pixels


if __name__ == "__main__":
	c = Rain(1.0)
