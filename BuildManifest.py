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

    0.1
"""

import sys, string
from string import Template
from urllib2 import Request, urlopen, URLError
import xml.etree.ElementTree as ET

TEMPLATE_FILE = 'AndroidManifest.template'
EXTRAS_FILE   = 'ExtraShorteners.txt'
DOMAIN_FMT    = '''                <data android:scheme="http"  android:host="${domain}" />
                <data android:scheme="https" android:host="${domain}" />'''
SERVICE_URL   = 'http://api.longurl.org/v2/services'

def formOne(domain):
    """ Format the intents for one host. """
    
    t = Template(DOMAIN_FMT)
    return t.substitute(domain=domain)

def formMany(domains):
    """ Returns a string for several hosts, to splice into the manifest. """

    return string.join(map(formOne, domains), '\n')    # Simply concatenate with newlines

def formManifest(domains):
    """ Read the template from file and splices in the domains. """

    f = open(TEMPLATE_FILE, 'r')
    fmt = f.read()
    f.close()

    t = Template(fmt)
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

def main(filename):
    """ Entry point for the script. """

    f = open(filename, mode='w')
    domains = fetchDomains()
    extraDomains = readExtraDomains()
    allDomains = sorted(set(domains + extraDomains))
    manstr = formManifest(allDomains)
    f.write(manstr.encode('UTF-8'))
    f.close()

if __name__ == '__main__':
    main(sys.argv[1])

