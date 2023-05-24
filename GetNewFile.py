import FnsEnginie
import settings
import os

def get_file_dif(base_file_list, new_filelist):
    a = set(line.strip() for line in open(base_file_list))
    b = set(line.strip() for line in open (new_filelist))
    c = b - a
    return c
    
def download_diff(fns_enginie: FnsEnginie.FnsDownload, set_to_download, target_dir):
    for f in set_to_download:
        fns_enginie.download_file(f, target_dir)

def download_new(fns_enginie: FnsEnginie.FnsDownload, base_file, new_file, target_dir):
    delta = get_file_dif(base_file, new_file)
    print(f'to download: {delta}')
    download_dif(fns_enginie, delta, target_dir)
    print('done to: ' + settings.FNS_dir_target)
    if os.path.isfile(base_file) and os.path.isfile(newfile):
        os.remove(base_file)
        os.remove(new_file, base_file)

if __name__ == '__main__' :
    print('GetNewFile started')
    fnsUL = FnsEnginie.FnsDownload(proxies = settings.proxies, headers = settings.headers,
            cert_str = settings.ulcert, key_srt = settings.ulkey, url = settings.url,
            start_dir = settings.ulstart_dir, ex_dir = settings.ex_dir)
    print('EGRUL enginie init done')
    fnsIP = FnsEnginie.FnsDownload(proxies = settings.proxies, headers = settings.headers,
            cert_str = settings.ipcert, key_srt = settings.ipkey, url = settings.url,
            start_dir = settings.ipstart_dir, ex_dir = settings.ex_dir)
    print('EGRIP enginie init done')
    fnsUL.file_list_to_file('EGRUL_last.txt')
    print('EGRUL_last.txt done')
    fnsIP.file_list_to_file('EGRIP_last.txt')
    print('EGRIP_last.txt done')
    download_new(fns_enfinie = fnsUL, base_file = 'EGRUL.txt', new_file = 'EGRUL_last.txt', target_dir = settings.FNS_dir_target)
    download_new(fns_enfinie = fnsIP, base_file = 'EGRIP.txt', new_file = 'EGRIP_last.txt', target_dir = settings.FNS_dir_target)
    print('ALL done')