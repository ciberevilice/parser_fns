url = 'https://ftp.egrul.nalog.ru/'
ulcert = '$186303_cert.pem'
ulkey = '$186303_key.pem' 
ulstart_dir = '?dir=EGRUL_406'
ipcert = '$186304_cert.pem'
ipkey = '$186304_key.pem'
ipstart_dir = '?dir=EGRUL_405'
#FNS_dir_local = '..\\..\\SrcFiles\FNS\\'
FNS_dir_local = '../../SrcFiles\FNS\'

headers = {'User-Agent': 'Mozilla/5.0'}
ex_dir = {
'_FULL',
'.2022'
}

#for local testing
#username = ''
#password = ''
#proxies = {'http': 'http://' + username + ':' + password + @url_Address:8080',
#           'https': 'http://' + username + ':' + password + @url_Address:8080'

proxies = {}