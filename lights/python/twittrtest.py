import twitter
import string
api = twitter.Api()

red = (1023, 0, 0)
green = (0, 1023, 0)
blue = (0, 0, 1023)
orange = (1023, 660, 0)
purple = (1023, 0, 1023)
yellow = (1023, 1023, 0)

class Search():
	def render(self, pixels):
		i = 0
		searchresult = api.GetSearch("blue OR orange OR yellow OR green OR purple OR red", per_page=1500)
		colormap = {"red":0, "green":0, "blue":0, "orange":0, "purple":0, "yellow":0}
		for s in searchresult:
			print s.text
			if (s.text.find("orange") > -1):
				colormap["orange"]+=1
			elif (s.text.find("blue") > -1):
				colormap["blue"]+=1
			elif (s.text.find("yellow") > -1):
				colormap["yellow"]+=1
			elif (s.text.find("green") > -1):
				colormap["green"]+=1
			elif (s.text.find("purple") > -1):
				colormap["purple"]+=1
			elif (s.text.find("red") > -1):
				colormap["red"]+=1
		print colormap
		return pixels

if __name__ == "__main__":
	#import octoapi
	import time
	pixels = [(0,0,0)]*8
	while True:
		s = Search()
		s.render(pixels)
		#octoapi.clear()
		#octoapi.write(pixels)
		time.sleep(1)
