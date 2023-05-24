import FnsEnginie
import settings

if __name__ == '__main__':
    print ('InitDileList started')
    fnsUL = FnsEnginie.FnsDownload(proxies = settings.proxies, headers = settings.headers,
            cert_str = settings.ulcert, key_srt = settings.ulkey, url = settings.url,
            start_dir = settings.ulstart_dir, ex_dir = settings.ex_dir)
    print('EGRUL enginie init done')
    fnsIP = FnsEnginie.FnsDownload(proxies = settings.proxies, headers = settings.headers,
            cert_str = settings.ipcert, key_srt = settings.ipkey, url = settings.url,
            start_dir = settings.ipstart_dir, ex_dir = settings.ex_dir)
    print('EGRIP enginie init done')
    fnsUL.file_list_to_file('EGRUL.txt')
    print('EGRUL.txt done')
    fnsIP.file_list_to_file('EGRIP.txt')
    print('EGRIP.txt done')

