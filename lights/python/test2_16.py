import animations


class Clock(animations.Animation):
    def __init__(self):
        self.num = 0

    def update(self, timeDiff):
        pass

    ##this assumes 6 pixels, 2-8 (our setup)
    def render(self, pixels):
        pixels = [(0,0,0)]*16
        pixels[self.num] = (1023,1023,1023)
        self.num+=1
        self.num = self.num % 16
        return pixels
    

if __name__ == "__main__":
    import octoapi
    import time
    c = Clock()
    while True:
        pix = [(0,0,0)]*16
        pix = c.render(pix)
        octoapi.write(pix)
        time.sleep(0.1)
