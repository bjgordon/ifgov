import unittest
from ifgovdb import IfGovDb
from datetime import datetime

class TestSequenceFunctions(unittest.TestCase):
  def setUp(self):
    self.db = IfGovDb()
    self.db.removeSubscriptionByName('jamestest')
    self.db.insertSubscription('jamestest', 0, 0, 1, 1, None, 1, 'jcaunce@gmail.com', '20140101', '0', '20140713', '1', '20140712', False)
    
  def tearDown(self):
    self.db.removeSubscriptionByName('jamestest')
    
  def test_getSubscriptionByName(self):
    sub = self.db.getSubscriptionByName('jamestest')
    self.assertEquals('jamestest', sub.name)
  
  def test_getNewSubscriptions(self):
    subs = self.db.getNewSubscriptions()
    self.assertTrue(len(subs) > 0)
    for sub in subs:
      if sub.name == 'jamestest':
        self.assertEquals('jcaunce@gmail.com', sub.notificationsettings)
        
  def test_getChangedSubscriptions(self):
    subs = self.db.getChangedSubscriptions()
    for sub in subs:
      self.assertTrue(sub.lastnotified < sub.currentupdate)
        
  def test_setNotified(self):
    starttime = datetime.now()
    sub = self.db.getSubscriptionByName('jamestest')
    self.db.setNotified(sub)
    sub = self.db.getSubscriptionByName('jamestest')
    self.assertTrue(sub.lastnotified > starttime)

if __name__ == '__main__':
    unittest.main()
