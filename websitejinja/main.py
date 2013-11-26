# Copyright 2012 Digital Inspiration
# http://www.labnol.org/
import os
import webapp2
import jinja2
from google.appengine.ext import webapp
from google.appengine.ext.webapp import util
from google.appengine.ext.webapp import template
from google.appengine.ext import ndb

TEMPLATE = "templates/base.html"
JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(TEMPLATE)),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

DEFAULT_DB_NAME = "EMAILS_DB"
FEEDBACK_DB_NAME = "FEEDBACK_DB"


print os.path.dirname(TEMPLATE)
def email_key(db_name=DEFAULT_DB_NAME):
    """Constructs a Datastore key for a Guestbook entity with guestbook_name."""
    return ndb.Key('Emails', db_name)

def feedback_key(db_name=FEEDBACK_DB_NAME):
    """Constructs a Datastore key for a Guestbook entity with guestbook_name."""
    return ndb.Key('Feedback', db_name)

class Feedback(ndb.Model):
    """Models an individual Guestbook entry with author, content, and date."""
    email = ndb.StringProperty()
    phone_model = ndb.StringProperty()
    feedback = ndb.TextProperty()
    date = ndb.DateTimeProperty(auto_now_add=True)



class Email(ndb.Model):
    """Models an individual Guestbook entry with author, content, and date."""
    email = ndb.StringProperty()
    date = ndb.DateTimeProperty(auto_now_add=True)


class MainHandler(webapp.RequestHandler):
  def get (self, q):
      if(q is None):
          template = JINJA_ENVIRONMENT.get_template('index.html')
      else:
          template = JINJA_ENVIRONMENT.get_template(q)
      self.response.write(template.render())

class EmailHandler(webapp2.RequestHandler):
  def post(self):
    emailbook_name = self.request.get('email_input', DEFAULT_DB_NAME)
    print "GOTHIS: "+emailbook_name
    emailEntity = Email(parent=email_key(emailbook_name))
    emailEntity.email = self.request.get('email_input')
    emailEntity.put()
    #query_params = {'emailbook_name': emailbook_name}
    #self.redirect('/')

class FeedbackHandler(webapp2.RequestHandler):
  def post(self):
    email = self.request.get('email_input', FEEDBACK_DB_NAME)
    email_input = self.request.get('email_input')
    phone_model = self.request.get('phone_model_input')
    feedback = self.request.get('feedback_input')
    #print "GOTHIS: "+email_input+" "+phone_model+" "+feedback
    
    feedbackEntity = Feedback(parent=feedback_key(email+feedback[0:min(10,len(feedback))]))
    feedbackEntity.email = self.request.get('email_input')
    feedbackEntity.phone_model = self.request.get('phone_model_input')
    feedbackEntity.feedback = self.request.get('feedback_input')
    
    feedbackEntity.put()

application = webapp.WSGIApplication ([('/(.*html)?', MainHandler),('/email',EmailHandler),('/feedback',FeedbackHandler)], debug=True)
#  util.run_wsgi_app (application)

