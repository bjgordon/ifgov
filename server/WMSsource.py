#!/usr/bin/python

import urllib2
import psycopg2
import urllib
import xml.etree.ElementTree
import json
import cgi
import os
import ConfigParser

dbname, user, password, host = (None, None, None, None)

def updateWMSValue(id,settings,lat,lon,currentVal,condition,conditionvalue, cur, conn):
  set = json.loads(settings)
  #url = set['url'] + '?service=WMS&request=GetFeatureInfo&version=1.1.1&srs=EPSG:3857&width=2&height=2&info_format=text/xml&x=1&y=1'+'&layers='+set['layers']+'&query_layers='+set['query_layers']
  url = set['url'] + '?service=WMS&request=GetFeatureInfo&version=1.1.1&srs=EPSG:4326&width=2&height=2&info_format=text/xml&x=1&y=1'+'&layers='+set['layers']+'&query_layers='+set['query_layers']
  #url = set['url'] + '?service=WMS&request=GetFeatureInfo&version=1.1.1&srs=EPSG:900913&width=2&height=2&info_format=text/xml&x=1&y=1'+'&layers='+set['layers']+'&query_layers='+set['query_layers']
  response = getWMSValue(url,lat,lon)
  #print response
  response = formatWMSValue(response)
  #print response
  if condition == 1:
    #why updating when ==?
    if json.dumps(response) != currentVal:
      #print "updating"
      #print currentVal
      #really should have option for source update as well as when updated in DB
      cur.execute("""UPDATE subscriptions set currentvalue= %s, lastvalue=currentvalue, lastupdate=currentupdate, currentupdate=now() where id= %s;""", (json.dumps(response),str(id)))
      conn.commit()

def updateValues():
  global dbname, user, password, host
  conn = psycopg2.connect("dbname=%s user=%s password=%s host=%s" % (dbname,user,password,host))
  cur = conn.cursor()
  cur.execute("""SELECT id,lat,lon,sourceid,currentValue,condition,conditionvalue from subscriptions""")
  for (id,lat,lon,sourceid,currentValue,condition,conditionvalue) in cur.fetchall():
    cur.execute("""SELECT type,settings from sources where id = %s ;""", (str(sourceid)))
    (type,settings) = cur.fetchone()
    if type == "wms":
      #print "going to update"
      updateWMSValue(id,settings,lat,lon,currentValue,condition,conditionvalue, cur, conn)
  cur.close()

#have password settings so generic
def getWMSValue(url,lat,lon):
  range = .00001
  minx = lon - range 
  miny = lat - range
  maxx = lon + range
  maxy = lat + range
  bbox = (minx,miny,maxx,maxy)
  url = url + "&bbox="+','.join(map(lambda x : str(x),bbox))
  #broadband doesn't like everything escaped :/
  #url = url.replace(" " , "%20")
  #print url
  #response = urllib2.urlopen(url)
  password_mgr = urllib2.HTTPPasswordMgrWithDefaultRealm()
  password_mgr.add_password(None, "programs.communications.gov.au", "wms", "wms")
  handler = urllib2.HTTPBasicAuthHandler(password_mgr)
  opener = urllib2.build_opener(handler)
  response = opener.open(url)
  return response.read()

#need a different parser for each source
def formatWMSValue(response):
  json = {}
  root = xml.etree.ElementTree.fromstring(response)
  namespace = {'mxp' : 'http://www.mapinfo.com/mxp', 'gml' : 'http://www.opengis.net/gml'}
  #feature = root.find(".//mxp:MultiFeatureCollection/mxp:FeatureCollection/mxp:FeatureMembers/mxp:Feature", namespaces = namespace)
  #for val in feature.findall(".//mxp:Val",namespaces=namespace):
  for val in root.findall(".//mxp:Val",namespaces=namespace):
    json[val.attrib["ref"]] = val.text
  del json['ESA_NAME']
  del json['ESA_DA']
  del json['ESA_CODE']
  del json['FILE_ID']
  del json['ESA_STATE']
  return json

if __name__ == '__main__':
  config = ConfigParser.ConfigParser()
  config.read('python.cfg')
  dbname = config.get('database', 'dbname')
  user = config.get('database', 'user')
  password = config.get('database', 'password')
  host = config.get('database', 'host')
  if 'GATEWAY_INTERFACE' in os.environ:
    form = cgi.FieldStorage()
    if 'sourceid' in form and 'lat' in form and 'lon' in form:
      conn = psycopg2.connect("dbname=%s user=%s password=%s host=%s" % (dbname,user,password,host))
      cur = conn.cursor()
      sourceid = form['sourceid'].value
      lat = float(form['lat'].value)
      lon = float(form['lon'].value)
      cur.execute("""SELECT type,settings from sources where id = %s;""", (str(sourceid)))
      (type,settings) = cur.fetchone()
      if type == "wms":
        set = json.loads(settings)
        url = set['url'] + '?service=WMS&request=GetFeatureInfo&version=1.1.1&srs=EPSG:4326&width=2&height=2&info_format=text/xml&x=1&y=1'+'&layers='+set['layers']+'&query_layers='+set['query_layers']
        print "Content-Type:application/json\n"
        print formatWMSValue(getWMSValue(url,lat,lon))
      cur.close()
    else:
      print "Content-Type:text\n"
      print "error"
  else:
    updateValues()


