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

def formOne(domain):
    """ Format the intents for one host. """
    
    fmt='''                <data android:scheme="http"  android:host="${domain}" />
                <data android:scheme="https" android:host="${domain}" />'''
    t = Template(fmt)
    return t.substitute(domain=domain)

def formMany(domains):
    """ Returns a string for several hosts, to splice into the manifest. """

    return string.join(map(formOne, domains), '\n')    # Simply concatenate with newlines

def formManifest(domains):
    """ Defines the template for the Manifest and splices in the domains. """

    fmt='''<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.github.nicolassmith.urlevaluator"
    android:versionCode="4"
    android:versionName="1.3" >

    <uses-sdk android:minSdkVersion="10" android:targetSdkVersion="17" />
    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name" 
        android:allowBackup="false">
        <activity
            android:name=".UrlEvaluatorActivity"
            android:label="@string/app_name"
            android:excludeFromRecents="true"
            android:theme="@android:style/Theme.Translucent.NoTitleBar">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
${domainData}
            </intent-filter>
        </activity>
    </application>

</manifest>
'''
    t = Template(fmt)
    return t.substitute(domainData=formMany(domains))

def fetchDomains():
    """ We get the list of domains from longurl.org """
    
    url = 'http://api.longurl.org/v2/services'
    req = Request(url)
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
        return [s.text for s in services]

def readExtraDomains():
    """ Loads from file extra shorteners that we like. """
    
    f = open('ExtraShorteners.txt','r')
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

