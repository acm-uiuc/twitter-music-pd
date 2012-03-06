import math
import random
import octoapi
import time
import thread
import sunshine

class Animation(object):

	def __init__(self):
		super(Animation, self).__init__()
		self.is_running = True
		self.pixels = []

	def update(self, timeDiff):
		pass

	def render(self, pixels):
		pass
	
	def destroy(self):
		self.is_running = False

class SCW(Animation):
	"""Represents cloudiness/sunshine/windiness"""

	def __init__(self, shine_type, speed, width):
		super(SCW, self).__init__()
		for i in range(0, 8):
			self.pixels.append((0,0,0))

		self.speed = float(speed)
		self.shine_type = shine_type
		self.position = 0.0
		self.direction = -1.0
		if width is None:
			self.width = random.random() * 5
		else:
			self.width = width
		self.thread_id = thread.start_new_thread(self.loop, ())

	def loop(self):
		while self.is_running:
			self.update(0.04)
			self.render()
			octoapi.write(self.pixels)
			time.sleep(.03)

	def update(self, timeDiff):
		self.position += self.direction * self.speed * timeDiff
		if (self.position < -1.):
			self.position = 2.
			width = random.random() * 5 
		#print "position: %f, speed: %f direction: %f"%(self.position, self.speed, self.direction)
	
	def render(self):
		size = float(len(self.pixels))
		qq = [0,0,0,0,0,0,0,0]
		for i,pixel in enumerate(self.pixels):
			pos = (i+0.5)/size
			intensity = max(0,min(1,(1-abs(pos-self.position))))
			intensity = math.pow(intensity,self.width)
			maxval = 1022
			val = maxval * intensity + 1
			if self.shine_type == "cloud":
				self.pixels[i] = (val, val, val)
			elif self.shine_type == "wind":
				val = 100
				if random.randint(0,1) == 0:
					val+=random.randint(0,50)
				else:
					val-=random.randint(0,20)

				self.pixels[i] = (0, 0, val)
			else:
				self.pixels[i] = (val, val, 0)
			qq[i] = intensity
		#print qq
		return self.pixels

class RSL(Animation):
	"""Represents Rain | Snow | Lightning, since their codebases are quite similar"""

	def __init__(self, precip_type, lightning, update_speed):
		super(RSL, self).__init__()
	
		self.update_speed = update_speed
		self.precip_type = precip_type
		self.lightning = lightning

		for i in range(0, 8):
			self.pixels.append((0,0,0))
		self.thread_id = thread.start_new_thread(self.loop, ())

	def loop(self):
		while self.is_running:
			self.render()
			octoapi.write(self.pixels)
			time.sleep(self.update_speed)

	def render(self):
		size = float(len(self.pixels))

		for i, pixel in enumerate(self.pixels):
			if (self.precip_type == "rain"):
				self.pixels[i] = fade(self.pixels[i],(0,0,0),0.91)
			else:
				self.pixels[i] = fade(self.pixels[i],(0,0,0),0.94)

		indexCount = random.randint(0, 7)
		for i in range(0, indexCount):
			randomIndex = random.randint(0, 7)
			if (self.precip_type == "rain"):
				if random.randint(0, 10) is 5:
					self.pixels[randomIndex] = fade(self.pixels[randomIndex],(0, 0, 100), 0.1)
			else:
				if random.randint(0, 2) is 1:
					self.pixels[randomIndex] = fade(self.pixels[randomIndex],(1023, 1023, 1023), 0.91)

		if self.lightning:
			if random.randint(0, 100) is 5:
				randomIndex = random.randint(0,7)
				self.pixels[randomIndex] = (1023, 1023, 1023)

		#for i in range(len(self.pixels)):
	#		self.pixels[i] = (0,0,1023)
		#print self.pixels
		return self.pixels


def fade(tup1, tup2, amount):
	a = 1-amount
	o = amount
	return (tup1[0]*o+tup2[0]*a,tup1[1]*o+tup2[1]*a,tup1[2]*o+tup2[2]*a)

if __name__ == "__main__":
	import sys
	import twittercolor
	numanims = 400
	sleeptime = 12
	pixels = [(0,0,0)]*8
	
	

	import test2
	c = test2.Clock()
	for x in range(48):
		pix = [(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0)]
		pix = c.render(pix)
		octoapi.write(pix)
		time.sleep(0.2)

	rain = RSL("rain", False, 0.03)
	time.sleep(sleeptime)
	rain.is_running = False

	print "rainlightning time"
	rainlightning = RSL("rain", True, 0.03)
	time.sleep(sleeptime)
	rainlightning.is_running = False

	print "snow time"
	snow = RSL("snow", False, 0.07)
	time.sleep(sleeptime)
	snow.is_running = False



	print "sunshine time"
	pixels = [(),(),(),(),(),(),(),()]
	c = sunshine.Sunshine(0.1, 1.0)
	
	import time
	for x in range(numanims):
		c.update(0.04)
		c.render(pixels)
		octoapi.write(pixels)
		time.sleep(0.03)
	
	s = twittercolor.Search()
	s.render(pixels)
	#octoapi.clear()
	octoapi.write(pixels)
	time.sleep(sleeptime)

	import clock
	import time
	c = clock.Clock()
	for x in range(numanims):
		pix = [(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0)]
		pix = c.render(pix)
		octoapi.write(pix)
		time.sleep(0.03)
