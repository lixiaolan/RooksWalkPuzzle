import webapp2
import random
import logging 
import xml.etree.ElementTree as ET
from google.appengine.api import memcache
import time
import datetime



class BaseHandler(webapp2.RequestHandler):
    def get(self):        
        puzzle = self.get_puzzle()

        
        self.response.headers['Content-Type'] = 'application/xml'
        self.response.write(puzzle)

    def get_puzzle(self):
        puzzle = memcache.get('puzzle')
        if puzzle != None:
            return puzzle
        else:
            #TODO: Modify this to get the actual daily puzzle
            tree=ET.parse('DailyPuzzles.xml')
            root = tree.getroot()
            puzzle_xml = root[datetime.datetime.now().timetuple().tm_yday]
            puzzle_xml.attrib['text'] = "^Daily Puzzle for "+time.strftime("%m/%d/%Y")
            puzzle = ET.tostring(puzzle_xml)

            if not memcache.add('puzzle',puzzle,3600):
                logging.error('Memcache set failed.')
            return puzzle

application = webapp2.WSGIApplication(
    [('/puzzle', BaseHandler)],
    debug=True
)


        # for group in select_groups:
        #     self.graph.put_wall_post(description, {"link":"http://facebook.com/%s"%(posted_id),"picture":images.get_serving_url(blob_key)}, group)
