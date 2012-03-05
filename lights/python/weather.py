import yql as YQL
from animations import RSL, SCW

class Weather():
    def __init__(self, zipCode):
        self.yql = YQL.Public()
        self.local_weather = self._get_weather(zipCode)

    def _construct_query(self, zipCode):
        return 'select * from weather.forecast where location=%d' % zipCode

    def _get_weather(self, zipCode):
        query = self._construct_query(zipCode)
        return self.yql.execute(query).rows

    def display_weather(self):
        weather_status =int(self.local_weather[0]["item"]["condition"]["code"])
        print weather_status

        if weather_status in [0, 1, 2, 23, 24]:
            obj = SCW("wind", 1.0, None)
        elif weather_status in [31, 32, 33, 34, 36]:
            obj = SCW("sun", .2, None)
        elif weather_status in [20, 21, 22, 26, 27, 28, 29, 30]:
            obj = SCW("cloud", .2, None)
        elif weather_status in [5, 6, 8, 9, 10, 11, 12, 35, 40]:
            obj = RSL("rain", False, .5)
        elif weather_status in [13, 14, 15, 16, 41, 42, 43, 46]:
            obj = RSL("snow", False, .5)
        elif weather_status in [3, 4, 37, 38, 39, 45, 47]:
            obj = RSL("rain", True, 5)
        else:
            return None
        return obj

if __name__ == "__main__":
    weather = Weather(61801)
    weather_type = weather.display_weather()
    while True:
        pass
