import animations
import time


if __name__ == "__main__":
    import octoapi
    import time
    while True:
        pix = [(723,1023,923)]*8
        octoapi.write(pix)
        time.sleep(0.2)
