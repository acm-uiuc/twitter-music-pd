import animations


class Clock(animations.Animation):
    def __init__(self):
        pass

    def update(self, timeDiff):
        pass

    ##this assumes 6 pixels, 2-8 (our setup)
    def render(self, pixels):
        offset = 2
        pixrange = 1023
        pixmin = 0
        (year,month,day,hours,minutes,seconds,blah,blah2,blah3) = time.localtime()
        sec1 = pixmin + pixrange * ((seconds % 10)/10.)
        pixels[offset+0] = self.valToRGB(sec1) 
        sec2 = pixmin + pixrange * ((seconds / 10)/10.)
        pixels[offset+1] = self.valToRGB(sec2)
        min1 = pixmin + pixrange * ((minutes % 10)/10.)
        pixels[offset+2] = self.valToRGB(min1)
        min2 = pixmin + pixrange * ((minutes / 10)/6.)
        pixels[offset+3] = self.valToRGB(min2)
        hour1 = pixmin + pixrange * ((hours % 10)/10.)
        pixels[offset+4] = self.valToRGB(hour1)
        hour2 = pixmin + pixrange * ((hours / 10)/1.)
        pixels[offset+5] = self.valToRGB(hour2)
        return pixels
    
    def valToRGB(self, val):
        if (val < 1023/4.0):
            return (val,val,0)
        elif (val < 1023/4.0*2.0):
            return (0,val,val)
        elif (val < 1023/4.0*3.0):
            return (val,0,val)
        else:
            return (val,val,val)


if __name__ == "__main__":
    import octoapi
    import time
    c = Clock()
    while True:
        pix = [(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0),(0,0,0)]
        pix = c.render(pix)
        octoapi.write(pix)
        time.sleep(0.2)
