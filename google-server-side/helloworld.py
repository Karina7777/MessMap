import webapp2

from google.appengine.ext import ndb
import json

class Mark(ndb.Model):
  """Models an individual Guestbook entry with content and date."""
  altitude = ndb.FloatProperty()
  longitude = ndb.FloatProperty()
  title = ndb.StringProperty()

def mark_key():
    return ndb.Key("OurMap", "map")
    
class AddMark(webapp2.RequestHandler):

    def post(self):
        altitude = float(self.request.get('altitude'))
        longitude = float(self.request.get('longitude'))
        title = self.request.get('title')    
        key = mark_key()
        print 'Key = ', key
        mark = Mark(parent=key)
        mark.longitude = longitude
        mark.altitude = altitude
        mark.title = title
        print 'Mark: ', mark
        mark.put()
        
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.write('Success')

class GetMark(webapp2.RequestHandler):

    def get(self):
        m_query = Mark.query(ancestor=mark_key())
        marks = m_query.fetch(10)

        print len(marks)

        self.response.headers['Content-Type'] = 'text/plain'

        arr = []

        for m in marks: 
          arr.append({'logitude':m.longitude, 'altitude': m.altitude, 'title': m.title})
        self.response.write(json.dumps(arr))


application = webapp2.WSGIApplication([
        ('/', GetMark),
        ('/add', AddMark),
        ], debug=True)

