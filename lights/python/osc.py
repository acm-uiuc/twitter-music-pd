from OSC import OSCServer, OSCClient, OSCMessage
import sys
import time
#import octoapi
# funny python's way to add a method to an instance of a class
import types


# this method of reporting timeouts only works by convention
# that before calling handle_request() field .timed_out is 
# set to False
def handle_timeout(self):
    self.timed_out = True


def set_colors(path, tags, args, source):
    # which user will be determined by path:
    # we just throw away all slashes and join together what's left
    print "Here's what we got : path:%s tags:%s args:%s source:%s"%(str(path),str(tags),str(args),str(source))
    pixels = []
    for i in range(0,len(args)/3):
        pixel = (args[i*3],args[i*3+1],args[i*3+2])
        pixels.append( pixel )
    #print "Pixels: %s"%str(pixels)
    #print "Time: "+str((time.time()*1000) % 10000)
    #octoapi.write(pixels)

def each_frame():
    server.timed_out = False
    while not server.timed_out:
        server.handle_request()

if __name__ == "__main__":
    #server = OSCServer( ("128.174.251.39", 11661) )
    server = OSCServer( ("localhost", 11661) )
    server.timeout = 0
    
    server.handle_timeout = types.MethodType(handle_timeout, server)
    server.lastpixels = [(0,0,0)]*24

    server.addMsgHandler( "/setcolors", set_colors)
    while True:
        each_frame()

    server.close()


class ColorsOut:
    def __init__(self):
        self.client = OSCClient()
        self.client.connect( ("localhost",11661) )

    def write(self, pixels):
        message = OSCMessage("/setcolors")
        message.append(pixels)
        self.client.send( message )
