import urlparse
import urllib
from bs4 import BeautifulSoup

url = "http://art.alphacoders.com/by_sub_category/167192"


urls =  [url] #list of urls to visit
visited = [url] #urls visited

def list_images(soup):
    for img in soup.findAll('img'):
        print img['src']

    
    
    
    


def main():
    
    while len(urls) > 0:
    
        try:
            htmltext = urllib.urlopen(urls[0]).read()
        except:
            print "error:"+urls[0]
        soup = BeautifulSoup(htmltext)
        print "found "+str(len(soup.findAll('img')))+" images in "+urls[0]
        list_images(soup)
        urls.pop(0)
        
        for tag in  soup.findAll('a',href=True):
            tag['href'] = urlparse.urljoin(url,tag['href'])
            if url in tag['href'] and tag['href'] not in visited:
                urls.append(tag['href'])
                visited.append(tag['href'])
                
main()
                
        
                        


        
