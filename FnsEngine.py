import request
from bs4 import BeautifulSoup
from request.packages.urllib3.exceptions import InsecureRequestWarning
import warnings

class FnsDownload:
    def __init__(self, proxies, headers, cert_str, key_str, url, start_dir, ex_dir):
        self.proxies = proxies
        self.headers = headers
        self.cert_str = cert_str
        self.key_str = key_str
        self.url = url
        self.start_dir = start_dir
        self.ex_dir = ex_dir
        request.packages.urllib3.disable_warnings(InsecureRequestWarning)
        warnings.simplefilter("ignore")
    
    def href_parsing(self, dir):
        responce = request.get(url = self.url+dir, proxies = self.proxies, 
                                headers = self.headers, verify = False, 
                                cert = (self.cert_str, self.key_str))
        soup = BeautifulSoup(responce.text, 'html.parser')
        href_kinks = []
        for link in soup.find_all(attrs = {'class' : 'clearfix'}):
            if str(link['href'])[0] == '?' or str(link['href']).lower()[-4:] == '.zip':
            href_links.append(link['href'])
        return href_links
    
    def get_file_list(self, dir):
        sub_folder = self.href_parsing(dir)
        file_list = []
        for directory in sub_folder:
            patt_cnt = 0
            for flt in self.ex_dir:
                if directory.find(flt) != -1:
                    patt_cnt += 1
                    break
            if patt_cnt == 0:
                file_list.append(self.href_parsing(directory))
        files = []
        for files_listing in file_list:
            for file in files_listing:
                files.append(file)
        return files
        
    def file_list_to_file(self, filename):
        file_list = self.get_file_list(self.start_dir)
        with open(filename, 'w') as f:
            for file in file_list:
                f.write(f'{file}\n')
    
    def get_file_name(self, string):
        file_name = string[string.rfind('/')+ 1:]
        return file_name
        
    def download_file(self, file_to_download, target_dir):
        response_zip = requests.get(self.url + file_to_download, proxies = self.proxies,
                                    headers = self.headers, verify = False, 
                                    cert = (self.cert_str, self.key_str))
        with open(target_dir + self.get_file_name(file_to_download), 'wb') as temp_file:
            temp_file.write(response_zip.content)
    
if __name__ == '__main__':
    print('To local test')
    