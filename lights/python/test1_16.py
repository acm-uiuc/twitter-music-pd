import animations
import time


if __name__ == "__main__":
    import octoapi
    import time
    while True:
        pix = [(1023,1023,1023)]*24
        octoapi.write(pix)
        time.sleep(0.2)
