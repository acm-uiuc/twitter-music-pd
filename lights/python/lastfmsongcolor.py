import yql
import json
import sys
from PIL import Image

y = yql.Public()


def getMAXCOLOR(image):
    colors = image.getcolors()
    colors.sort()
    return colors[0]

if __name__=="__main__":
    artist = "Metallica"
    track = "One"
    query = 'USE "http://www.datatables.org/lastfm/lastfm.track.search.xml"; select * from lastfm.track.search where api_key="65f6b172284642317f386345b67e7315" and artist="%s" and track="%s"'%(artist,track)
    print "Query: %s"%query
    print "Executing now..."
    result = y.execute(query)
    print "Done executing!"

    trackmatches = result.rows[0]["results"]["trackmatches"]["track"]
    track = trackmatches[0]
    images = track["image"]
    imageurl = ""
    for image in images:
        if image["size"] == "small":
            imageurl = image["content"]

    if not imageurl:
        pass


    im = Image.open(imageurl)
    boxsize = im.width/6
    colors = []
    for i in range(6):
        crop = im.crop(i*boxsize, i*boxsize, (i+1)*boxsize, (i+1)*boxsize)
        color = getMAXCOLOR(crop)
        colors.append(color)
    print colors
