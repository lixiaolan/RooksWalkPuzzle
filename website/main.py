# Copyright 2012 Digital Inspiration
# http://www.labnol.org/
import os
import webapp2
from google.appengine.ext import webapp
from google.appengine.ext.webapp import util
from google.appengine.ext.webapp import template
from google.appengine.ext import ndb

DEFAULT_DB_NAME = "EMAILS_DB"

def email_key(db_name=DEFAULT_DB_NAME):
    """Constructs a Datastore key for a Guestbook entity with guestbook_name."""
    return ndb.Key('Emails', db_name)


class Email(ndb.Model):
    """Models an individual Guestbook entry with author, content, and date."""
    email = ndb.StringProperty()
    date = ndb.DateTimeProperty(auto_now_add=True)


class MainHandler(webapp.RequestHandler):
  def get (self, q):
    if q is None:
      q = 'index.html'
    path = os.path.join (os.path.dirname (__file__), q)
    self.response.headers ['Content-Type'] = 'text/html'
    self.response.out.write (template.render (path, {}))

class EmailHandler(webapp2.RequestHandler):
  def post(self):
    emailbook_name = self.request.get('email_input', DEFAULT_DB_NAME)
    print "GOTHIS: "+emailbook_name
    emailEntity = Email(parent=email_key(emailbook_name))
    emailEntity.email = self.request.get('email_input')
    emailEntity.put()
    #query_params = {'emailbook_name': emailbook_name}
    #self.redirect('/')

application = webapp.WSGIApplication ([('/(.*html)?', MainHandler),('/email',EmailHandler)], debug=True)
#  util.run_wsgi_app (application)

