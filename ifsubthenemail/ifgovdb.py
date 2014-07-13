import psycopg2
import psycopg2.extras
import json
from datetime import datetime

class IfGovDb:
  SQL = "select a.id id, "\
  "a.name subname, "\
  "a.lat lat, "\
  "a.lon lon, "\
  "s.name source, "\
  "n.name notification, "\
  "a.notificationsettings notificationsettings, "\
  "c.name condition, "\
  "a.conditionvalue conditionvalue, "\
  "a.lastnotified lastnotified, "\
  "a.lastupdate lastupdate, "\
  "a.lastvalue lastvalue, "\
  "a.currentupdate currentupdate, "\
  "a.currentvalue currenctvalue "\
  "from subscriptions a "\
  "left join sources s on a.sourceid = s.id "\
  "left join notifications n on a.notification = n.id "\
  "left join conditions c on a.condition = c.id"

  def __init__(self):
    self.conn = psycopg2.connect(database="ifgovthen", user="postgres", password="v758e0p.L{jJ7tS")
    
  def __cursor(self):
    return self.conn.cursor()
    
  def __dictcursor(self):
    return self.conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
    
  def close(self):
    self.conn.close()
    
  def removeSubscriptionByName(self, name):
    cur = self.__cursor()
    sql = "delete from subscriptions where name = %s;"
    params = [name]
    cur.execute(sql, params)
    self.conn.commit()
    cur.close()      
    
  def insertSubscription(self, name, lat, lon, sourceid, condition, conditionvalue, notification, notificationsettings, lastupdate, lastvalue, currentupdate, currentvalue, lastnotified, welcomed):
    cur = self.__cursor()
    sql = "insert into subscriptions (name, lat, lon, sourceid, condition, conditionvalue, notification, notificationsettings, lastupdate, lastvalue, currentupdate, currentvalue, lastnotified, welcomed) values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);"
    params = [name, lat, lon, sourceid, condition, conditionvalue, notification, notificationsettings, lastupdate, lastvalue, currentupdate, currentvalue, lastnotified, welcomed]
    cur.execute(sql, params)
    self.conn.commit()
    cur.close()  

  def getSubscriptionByName(self, name):
    cur = self.__dictcursor()
    sql = self.SQL + " where a.name = %s;"
    cur.execute(sql, [name])
    sub = cur.fetchone()
    cur.close()    
    return Subscription(sub)

  def getNewSubscriptions(self):
    cur = self.__dictcursor()
    sql = self.SQL + " where a.welcomed = False or a.welcomed is null;"
    cur.execute(sql)
    rows = cur.fetchall()
    cur.close()

    subs = []
    for row in rows:
      subs.append(Subscription(row))

    return subs
    
  def getChangedSubscriptions(self):
    cur = self.__dictcursor()
    sql = self.SQL + " where a.lastnotified < a.currentupdate;"
    cur.execute(sql)
    rows = cur.fetchall()
    cur.close()

    subs = []
    for row in rows:
      subs.append(Subscription(row))

    return subs
    
  def setWelcomed(self, subscription):
    cur = self.__cursor()
    cur.execute("update subscriptions set welcomed = True where id = %s;", [subscription.id])
    self.conn.commit()
    cur.close()
    
  def setNotified(self, subscription):
    cur = self.__cursor()
    cur.execute("update subscriptions set lastnotified = %s where id = %s;", [datetime.now(), subscription.id])
    self.conn.commit()
    cur.close()

class Subscription:
  def __init__(self, row):
    self.id = row[0]
    self.name = row[1]
    self.lat = row[2]
    self.lon  = row[3]
    self.source = row[4]
    self.notification = row[5]
    self.notificationsettings = row[6]
    self.condition = row[7]
    self.conditionvalue = row[8]
    self.lastnotified = row[9]
    self.lastupdate = row[10]
    self.lastvalue = row[11]
    self.currentupdate = row[12]
    self.currentvalue = row[13]
