#!/usr/bin/env python
"""
SYNOPSIS

    BuildManifest path/to/AndroidManifest.xml

DESCRIPTION

    Outputs a manifest for urlevaluator.

AUTHOR

    Leo C. Stein
    leo.stein@gmail.com

VERSION

    0.2
"""

import sys, string
from string import Template
from urllib2 import Request, urlopen, URLError
import xml.etree.ElementTree as ET

EXTRAS_FILE   = 'ExtraShorteners.txt'
DOMAIN_FMT    = '''\
                <data android:scheme="http"  android:host="${domain}" />
                <data android:scheme="https" android:host="${domain}" />'''
SERVICE_URL   = 'http://api.longurl.org/v2/services'

def formOne(domain):
    """ Format the intents for one host. """
    
    t = Template(DOMAIN_FMT)
    return t.substitute(domain=domain)

def formMany(domains):
    """ Returns a string for several hosts, to splice into the manifest. """

    return string.join(map(formOne, domains), '\n')    # Simply concatenate with newlines

def formManifest(domains,templateString):
    """ Read the template from file and splices in the domains. """

    t = Template(templateString)
    return t.substitute(domainData=formMany(domains))

def fetchDomains():
    """ We get the list of domains from longurl.org """
    
    req = Request(SERVICE_URL)
    try:
        response = urlopen(req)
    except URLError as e:
        if hasattr(e, 'reason'):
            print 'We failed to reach a server.'
            print 'Reason: ', e.reason
        elif hasattr(e, 'code'):
            print 'The server couldn\'t fulfill the request.'
            print 'Error code: ', e.code
        sys.exit(-1)
    else:
        # everything is fine
        xmlstr = response.read()
        
        tree = ET.fromstring(xmlstr)
        services = tree.findall('.//service')
        return [s.text for s in services if s.attrib['regex']=='']

def readExtraDomains():
    """ Loads from file extra shorteners that we like. """
    
    f = open(EXTRAS_FILE,'r')
    domains = [l.rstrip() for l in f.readlines()]
    f.close()
    return domains

def makeTemplate(filename):
    """ takes filename of the manifest file and builds a template to replace data tags """
    activityName = ".UrlEvaluatorActivity"
    templateVariable = "${domainData}"
    nameTag = "{http://schemas.android.com/apk/res/android}name"
    
    manifestTree = ET.parse(filename)

    # find the correct intent filter element
    activity = [act for act in manifestTree.findall("application/activity") if act.attrib[nameTag]==activityName][0]
    intentFilter = [child for child in activity.getchildren() if child.tag=="intent-filter"][0]

    #remove all the data tags
    dataTags = [element for element in intentFilter if element.tag=="data"];
    for element in dataTags:
        intentFilter.remove(element)

    # insert the tag that will be template-replaced
    lastElement = intentFilter[-1];
    lastElement.tail = "\n" + templateVariable + "\n           "
    
    # for some reason it forgets the namespace tag for android, and doesn't carry the ?xml tag to the end
    templateString = ET.tostring(manifestTree.getroot()).replace("ns0","android")
    templateString = '<?xml version="1.0" encoding="utf-8"?>\n'+templateString+'\n'
    return templateString

def main(filename):
    """ Entry point for the script. """

    domains = fetchDomains()
    extraDomains = readExtraDomains()
    allDomains = sorted(set(domains + extraDomains))

    templateString = makeTemplate(filename)
    
    manstr = formManifest(allDomains,templateString)
    
    f = open(filename, mode='w')
    f.write(manstr.encode('UTF-8'))
    f.close()

if __name__ == '__main__':
    main(sys.argv[1])

