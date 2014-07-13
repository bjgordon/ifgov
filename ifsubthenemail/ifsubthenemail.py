#!/usr/bin/python

from ifgovdb import IfGovDb
from mail import Mailer
import logging
from datetime import datetime

logging.basicConfig(filename='/home/bitnami/ifsubthenemail/ifgov.log', level=logging.INFO)
logging.info("Started at " + str(datetime.now()))

mailer = Mailer()
db = IfGovDb()

for s in db.getNewSubscriptions():
  if s.notificationsettings != None:
    address = s.notificationsettings
    message = "You have registered a new notification with ifGov.  If at (" + str(s.lon) + "," + str(s.lat) + ") when " + s.source + " is changed then email me." 
    logging.info("Emailing " + address + ": " + message)
    mailer.sendMail([address],"noreply@ifgov.gordcorp.com","New Subscription",message);
    db.setWelcomed(s)
  else:
    logging.warn("No email address for " + s.name)

for s in db.getChangedSubscriptions():
  if s.notificationsettings != None:
    address = s.notificationsettings
    message = s.source + " was " + s.lastvalue + " and is now " + s.currentvalue + "."
    logging.info("Emailing " + address + ": " + message)
    mailer.sendMail([address],"noreply@ifgov.gordcorp.com","Changed value",message);
    db.setNotified(s)
  else:
    logging.warn("No email address for " + s.name)

db.close()
