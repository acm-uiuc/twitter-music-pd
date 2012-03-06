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
		searchresult = api.GetSearch("blue OR orange OR yellow OR green OR purple OR red")
		for s in searchresult:
			print s.text
			i = i+1
			if (s.text.find("orange") > -1):
				print("orange")
				pixels[i] = orange
			elif (s.text.find("blue") > -1):
				print("blue")
				pixels[i] = blue
			elif (s.text.find("yellow") > -1):
				print("yellow")
				pixels[i] = yellow
			elif (s.text.find("green") > -1):
				print("green")
				pixels[i] = green
			elif (s.text.find("purple") > -1):
				print("purple")
				pixels[i] = purple
			elif (s.text.find("red") > -1):
				print("red")
				pixels[i] = red
			if( i > 6):
				i = 0
		return pixels

if __name__ == "__main__":
	import octoapi
	import time
	pixels = [(0,0,0)]*8
	while True:
		s = Search()
		s.render(pixels)
		#octoapi.clear()
		octoapi.write(pixels)
		time.sleep(1)
