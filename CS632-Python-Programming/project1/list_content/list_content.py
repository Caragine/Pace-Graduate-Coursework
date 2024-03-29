"""
03-17-2021
CS 632P Topics: Python Programming Spring 2021
Prof. Sarbanes
Project #1
Group #9
-Dongphil Yoo -- drv, -d, drv, fld, -l
-Adam Caragine -- argparse paths, -f, fil
-Katie Albany -- fld, -l
-Darshan Venkata Bhojaraj-- typ, -t
"""

# import modules
import os, argparse, logging, string, shutil, math, pathlib, glob
from datetime import datetime
# os, pathlib, glob used for retrieving file system information
# argparse used to handle input arguments in console
# logging used to log program information
# shutil used to get disk storage information
# math used to convert size to bytes
# string used in function to get drives
# datetime used to convert modified timestamp to more readable format


# set up logging to file
logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(name)s %(levelname)-8s %(message)s',
                    datefmt='%Y-%m-%d %H:%M:%S',
                    filename='./list_content_log2.log',
                    filemode='a')
# get logger
logger = logging.getLogger()

# create parser
parser = argparse.ArgumentParser(description='Disk and File Information from Machine')

# add mutually exclusive group
group = parser.add_mutually_exclusive_group()

# add arguments
group.add_argument('-v', '--verbose', action='count', default=0, help='Modify output verbosity (-v, -vv)')
group.add_argument('-q', '--quiet', action='store_true', help='Quiet mode')

parser.add_argument('-d', action='store_true', help='List all drives and details')
parser.add_argument('-l', action='store_true', help='List all folders in a drive')
parser.add_argument('-f', action='store_true', help='List all files on Machine')
parser.add_argument('-t', action='store_true', help='list files by type')

parser.add_argument('path', nargs='*', help='Input a path for Drive (D:), Folder (D:/PycharmProjects), File (D:/document.txt), or File Type (.txt) to list info.')
#parser.add_argument('drv', help='List drive info')
#parser.add_argument('fld', help='List folder info')
#parser.add_argument('fil', help='List one file info')
#parser.add_argument('typ', help='list one file type info')

# parsing arguments
args = parser.parse_args()


# function returns all the drives in a machine
def get_drive():
    if os.name == 'nt':  # Windows
        list_drives = ['%s:' % d for d in string.ascii_uppercase if os.path.exists('%s:' % d)]
        return list_drives
    elif os.name == 'posix':  # Mac
        list_drives = ['/']
        return list_drives
    else:
        logger.critical('Cannot determine operating system')
        print('Cannot determine operating system\n')
        return False  # cross-platform implementation needed

def isDrive(path):
    if path in get_drive():
        return True
    else:
        return False

# function returns the number of files and directories in a certain path(drive)
def count_content(paths):
    total_dir = 0
    total_files = 0
    if type(paths) is list:
        list_total_dir_files = []
        for i in range(len(paths)):
            for root, dirs, files in os.walk(paths[i]):  # generates file names in a directory tree
                # print('Searching in: ', root)
                for dir in dirs:
                    total_dir += 1
                for file in files:
                    total_files += 1
            list_total_dir_files.append([total_dir, total_files])
            total_dir = 0
            total_files = 0
        return list_total_dir_files
    elif type(paths) is str:
        for root, dirs, files in os.walk(paths):
            for dir in dirs:
                total_dir += 1
            for file in files:
                total_files += 1
        return total_dir, total_files


# function returns disk usage info
def get_disk_usage(paths):
    if type(paths) is list:
        list_disk_usage = []

        for path in paths:
            if os.path.isdir(path):
                total, used, free = shutil.disk_usage(path)
                list_disk_usage.append([total, used, free])
            else:
                return False
        return list_disk_usage

    elif type(paths) is str:
        if os.path.isdir(paths):
            total, used, free = shutil.disk_usage(paths)
            return total, used, free
        else:
            return False


# function returns converted disk memory size
def convert_size(size_bytes):
    try:
        if size_bytes == 0:
            return '0B'

        size_name = ('B', 'KB', 'MB', 'GB', 'TB')
        i = int(math.floor(math.log(size_bytes, 1024)))
        p = math.pow(1024, i)
        s = round(size_bytes / p, 2)
        if s:
            return s, size_name[i]
    except Exception:
        logger.error('Cannot calculate disk storage memory')
        print('Cannot calculate disk storage memory\n')


# function gets folder info
def get_dir_info(path, get_all=False):
    try:
        if os.path.exists(path):
            path = os.path.abspath(path)
            total_file = 0
            sub_dirs = 0
            storage = get_directory_size(path)

            for entry in os.scandir(path):
                if entry.is_file():
                    total_file += 1
                if entry.is_dir():
                    sub_dirs += 1
            if get_all:
                for entry in os.scandir(path):
                    if entry.is_file():
                        total_file += 1
                    if entry.is_dir():
                        sub_dirs +=1
                        get_dir_info(entry)

            if args.verbose:  # if verbose on
                verbosity = args.verbose

                if verbosity > 1:  # -vv
                    logger.debug(f'Searching in {path}')
                    logger.info(f'There are total {total_file} files and the storage size is {convert_size(storage)[0]}{convert_size(storage)[1]}')
                else:  # -v
                    logger.debug(f'Searching in {path}')
                    logger.info(f'{total_file} files in {convert_size(storage)[0]}{convert_size(storage)[1]} storage')
            elif args.quiet:  # -q
                logger.info(f'{path} | {total_file} | {convert_size(storage)[0]}{convert_size(storage)[1]}')
            else:  # no -v and -q
                logger.info(f'FOLDER: {path} FILES: {total_file} STORAGE: {convert_size(storage)[0]}{convert_size(storage)[1]}')

            print("FOLDER NAME", "|", "FOLDER SIZE", "|", "TOTAL SUBDIRECTORIES", "|", "TOTAL FILES")
            print(f'{path} | {convert_size(storage)[0]}{convert_size(storage)[1]} | {sub_dirs} | {total_file}\n')
        else:
            print(f'Cannot read folder {path}\n')
            logging.warning(f'Cannot read folder {path}')

    except:
        logging.warning("Path Could Not Be Read")

# function gets folder size (https://stackoverflow.com/questions/1392413/calculating-a-directorys-size-using-python)
def get_directory_size(dir):
    total = 0

    try:
        for entry in os.scandir(dir):
            if entry.is_file():
                # if it's a file, use stat() function
                total += entry.stat().st_size
            elif entry.is_dir():
                # if it's a directory, recursively call this function
                total += get_directory_size(entry.path)
    except NotADirectoryError:
        # if `directory` isn't a directory, get the file size then
        logging.warning(f'{dir} is not a directory')
        return os.path.getsize(dir)
    except PermissionError:
        # if for whatever reason we can't open the folder, return 0
        return 0

    return total

# function returns file type
def list_file_type(paths, specify_file_type='*'):
    # paths can be boolean, if get_drive() was not implemented for the platform, else it should be a array

    results = {}  # { '.sql' : {'count':1, size: 123}}
    if paths and len(paths) > 0:
        for path in paths:
            for f in glob.iglob(f'{path}**/*{specify_file_type}', recursive=True):
                # print(f)
                file = pathlib.Path(f)
                file_extension = file.suffix
                file_size = 0
                try:
                    file_size = file.stat().st_size
                except FileNotFoundError as e:
                    logger.error(f"file Not found {f}")
                except OSError as e:
                    logger.error(f"OSError for file {f}")
                except:
                    logger.error(f"catch all for file {f}")
                if results.get(file_extension) is None:
                    results[file_extension] = {'count': 1, 'size': file_size}
                else:
                    res = results.get(file_extension)
                    res['count'] = res['count'] + 1
                    res['size'] = res['size'] + file_size

    return results

# function prints file type results
def print_list_file_types(results):
    if len(results) > 0:
        print("FILE TYPE", "|", "COUNT", "|", "FILE SIZE")

        for file_type in results:
            file_size = results.get(file_type).get("size")
            conv_file_size = convert_size(file_size)[0]
            unit = convert_size(file_size)[1]

            # print(file_type, "|", results.get(file_type).get('count'), "|", results.get(file_type).get('size'), "\n")
            print(file_type, "|", results.get(file_type).get('count'), "|", conv_file_size, unit, "\n")

            if args.verbose:  # if verbose on
                verbosity = args.verbose

                if verbosity > 1:  # -vv
                    logger.info(f'There are total {results.get(file_type).get("count")} of file type {file_type} using {conv_file_size}{unit} in a machine.')
                else:  # -v
                    logger.info(f'FILE TYPE: {file_type} TOTAL: {results.get(file_type).get("count")} STORAGE: {conv_file_size}{unit}')
            elif args.quiet:  # -q
                # logger.debug('Quiet mode on')
                logger.info(f'{file_type} {results.get(file_type).get("count")} {conv_file_size}{unit}')
            else:  # no -v and -q
                logger.info(f'{file_type} | {results.get(file_type).get("count")} | {conv_file_size}{unit}')


# main function
def main():
    print('\nProcessing, Please do not exit program!\n')

    if args.verbose:
        logger.debug('Verbose on')
    elif args.quiet:
        logger.debug('Quiet mode on')

    #if len(args.path) != 0:
    if args.__contains__("path") and len(args.path) != 0:
        for arg in args.path:
            if isDrive(arg): #drv
                print("INPUT RECOGNIZED AS VALID DRIVE")
                path = arg
                d = get_drive()

                if path in d:  # if drive exist
                    c = count_content(path)
                    u = get_disk_usage(path)

                    total_dir = c[0]
                    total_file = c[1]

                    total_memory = convert_size(u[0])
                    used_memory = convert_size(u[1])
                    free_memory = convert_size(u[2])

                    print("DRIVE", "|", "TOTAL DIRECTORIES", "|", "TOTAL FILES", "|", "STORAGE TOTAL", "|", "STORAGE USED", "|",
                          "STORAGE FREE")

                    if args.verbose:  # if verbose on
                        verbosity = args.verbose

                        if verbosity > 1:  # -vv
                            logger.debug(f'Searching all the directories and files in {path}')
                            logger.info(f'There are total {total_dir} directories and {total_file} files in {path}')
                            logger.info(
                                f'{path} has memory space of {total_memory[0]} {total_memory[1]} Total {used_memory[0]} {used_memory[1]} Used {free_memory[0]} {free_memory[1]} Free')
                        else:  # -v
                            logger.debug(f'Searching in {path}')
                            logger.info(f'{total_dir} directories {total_file} files')
                            logger.info(
                                f'{total_memory[0]} {total_memory[1]} Total {used_memory[0]} {used_memory[1]} Used {free_memory[0]} {free_memory[1]} Free')
                    elif args.quiet:  # -q
                        logger.info(
                            f'{path} d {total_dir} f {total_file} {used_memory[0]}/{total_memory[0]}{total_memory[1]} used {free_memory[0]}{free_memory[1]} free')
                    else:  # no -v and -q
                        logger.info(
                            f'{path} {total_dir} directories {total_file} files {total_memory[0]} {total_memory[1]} Total {used_memory[0]} {used_memory[1]} Used {free_memory[0]} {free_memory[1]} Free')

                    print(
                        f'{path} | {total_dir} | {total_file} | {total_memory[0]} {total_memory[1]} | {used_memory[0]} {used_memory[1]} | {free_memory[0]} {free_memory[1]}\n')
                else:
                    print(f'{path} not exists\nAppropriate drive name is needed\n')
                    logger.warning(f'{path} not exists')

            elif os.path.isdir(arg): #fld
                print("INPUT RECOGNIZED AS VALID FOLDER")
                get_dir_info(arg)

            elif os.path.isfile(arg): #fil
                print("INPUT RECOGNIZED AS VALID FILE")
                try:
                        path = arg
                        fileName = (os.path.split(path)[1]).split('.')[0] # os.path.split splits pathname after the last '/', then split('.') splits the extension off
                        fileType = os.path.splitext(path)[1]  # os.splitext splits pathname at the extension
                        fileSize = str((convert_size(os.stat(path).st_size)[0])) + (convert_size(os.stat(path).st_size)[1])  # os.stat.st_size gives file size in bytes, convert_size() converts it
                        fileDate = datetime.fromtimestamp((os.stat(path).st_mtime)).strftime('%m/%d/%Y %I:%M %p')  # os.stat.st_mtime gives modified timestamp, datetime used to format
                        result = str(fileName + " | " + fileType + " | " + fileSize + " | " + fileDate)

                       # print("FILE NAME", "|", "FILE TYPE", "|", "FILE SIZE", "|", "MODIFIED ON")

                        if args.verbose:
                            verbosity = args.verbose

                            if verbosity > 1:
                                print(f'The file named {fileName} is a {fileType} file and is {fileSize}. It was last modifided on {fileDate}.')
                                logger.info(
                                    f'The file named {fileName} is a {fileType} file and is {fileSize}. It was last modifided on {fileDate}.')
                            else:
                                print(f'File Name: {fileName} | File Type: {fileType} | File Size: {fileSize} | Modified Date: {fileDate}')
                                logger.info(
                                    f'File Name: {fileName} | File Type: {fileType} | File Size: {fileSize} | Modified Date: {fileDate}')
                        elif args.quiet:
                            print(str(fileName + " " + fileType + " " + fileSize + " " + fileDate))
                            logger.info(str(fileName + " " + fileType + " " + fileSize + " " + fileDate))
                        else:
                            logger.info(result)
                            print("FILE NAME", "|", "FILE TYPE", "|", "FILE SIZE", "|", "MODIFIED ON")
                            print(f'{result}\n')


                except FileNotFoundError as e:
                    print(f"File Could Not Be Read {path}\n")
                    logging.warning(e)

            elif (arg[0] == "."): #typ
                print("INPUT RECOGNIZED AS VALID EXTENSION / FILE TYPE")
                logger.info(f"Checking drives on basis of type of file")
                drives = get_drive()
                base_drives = [drive for drive in drives]
                results = list_file_type(base_drives, arg)
                print_list_file_types(results)

            else:
                print("Path could not be found \n")
                logging.warning(f"File Could Not Be Read")


    if args.l:  # -l command entered
        if args.path: # D:
            for arg in args.path:
                if isDrive(arg):
                    get_dir_info(arg, True)
                    return False

    if args.d:  # -d
        paths = get_drive()

        if paths:  # if drive exist
            total_dir_file = count_content(paths)
            disk_usage = get_disk_usage(paths)

            print("DRIVE", "|", "TOTAL DIRECTORIES", "|", "TOTAL FILES", "|", "STORAGE TOTAL", "|", "STORAGE USED", "|","STORAGE FREE")

            for i in range(len(paths)):
                total_dir = total_dir_file[i][0]
                total_file = total_dir_file[i][1]

                total_memory = convert_size(disk_usage[i][0])
                used_memory = convert_size(disk_usage[i][1])
                free_memory = convert_size(disk_usage[i][2])

                if args.verbose:  # if verbose on
                    # logger.debug('Verbose on')
                    verbosity = args.verbose

                    if verbosity > 1:  # -vv
                        logger.debug(f'Searching all the directories and files in {paths[i]}')
                        logger.info(f'There are total {total_dir} directories and {total_file} files in {paths[i]}')
                        logger.info(f'{paths[i]} has memory space of {total_memory[0]} {total_memory[1]} Total {used_memory[0]} {used_memory[1]} Used {free_memory[0]} {free_memory[1]} Free')
                    else:  # -v
                        logger.debug(f'Searching in {paths[i]}')
                        logger.info(f'{total_dir} directories {total_file} files')
                        logger.info(f'{total_memory[0]} {total_memory[1]} Total {used_memory[0]} {used_memory[1]} Used {free_memory[0]} {free_memory[1]} Free')
                elif args.quiet:  # -q
                    # logger.debug('Quiet mode on')
                    logger.info(f'{paths[i]} d {total_dir} f {total_file} {used_memory[0]}/{total_memory[0]}{total_memory[1]} used {free_memory[0]}{free_memory[1]} free')
                else:  # no -v and -q
                    logger.info(f'{paths[i]} {total_dir} directories {total_file} files {total_memory[0]} {total_memory[1]} Total {used_memory[0]} {used_memory[1]} Used {free_memory[0]} {free_memory[1]} Free')


                print(f'{paths[i]} | {total_dir} | {total_file} | {total_memory[0]} {total_memory[1]} | {used_memory[0]} {used_memory[1]} | {free_memory[0]} {free_memory[1]}\n')

        else:
            logger.critical('Cannot read file system')
            print('Cannot read file system')

    if args.f:  # -f command entered
        try:
            drives = get_drive()
            basedrives = [drive + "/" for drive in drives]
            for basedrive in basedrives:
                if os.path.exists(basedrive):
                    for dir, sub, files in os.walk(basedrive): #tuple of dirpath(string), dirnames(list) and filenames(list)
                        for f in files:
                            path = os.path.join(dir, f)
                            if os.path.isfile(path):
                                fileName = (os.path.split(path)[1]).split('.')[0]  # os.path.split splits pathname after the last '/', then split('.') splits the extension off
                                fileType = os.path.splitext(path)[1]  # os.splitext splits pathname at the extension
                                fileSize = str((convert_size(os.stat(path).st_size)[0])) + (convert_size(os.stat(path).st_size)[1])  # os.stat.st_size gives file size in bytes, convert_size() converts it
                                fileDate = datetime.fromtimestamp((os.stat(path).st_mtime)).strftime('%m/%d/%Y %I:%M %p')  # os.stat.st_mtime gives modified timestamp, datetime used to format
                                result = str(basedrive + " | " + fileName + " | " + fileType + " | " + fileSize + " | " + fileDate)

                                print("DRIVE", "|", "FILE NAME", "|", "FILE TYPE", "|", "FILE SIZE", "|", "MODIFIED ON")

                                if args.verbose:
                                    verbosity = args.verbose

                                    if verbosity > 1:
                                        # print(f'The file named {fileName} in {basedrive} is a {fileType} file and is {fileSize} in size. It was last modifided on {fileDate}.')
                                        logger.info(f'The file named {fileName} in {basedrive} is a {fileType} file and is {fileSize} in size. It was last modifided on {fileDate}.')
                                    else:
                                        # print(f'File Name: {fileName} | File Type: {fileType} | File Size: {fileSize} | Modified Date: {fileDate}')
                                        logger.info(f'File Name: {fileName} | File Type: {fileType} | File Size: {fileSize} | Modified Date: {fileDate}')
                                elif args.quiet:
                                    # print((str(fileName + " " + fileType + " " + fileSize + " " + fileDate)))
                                    logger.info(str(fileName + " " + fileType + " " + fileSize + " " + fileDate))
                                else:
                                    # print(result)
                                    logger.info(result)


                                print(f'{result}\n')
                            else:
                                print("File Could Not Be Read\n")
                                logging.warning(f"File Could Not Be Read {path}")
        except FileNotFoundError:
            logging.warning("File Could Not Be Read")

    if args.t:  # -t
        logger.info(f"Checking drives with -t arguments")
        drives = get_drive()
        #base_drives = [drive + "/" for drive in drives]
        base_drives = ["C:/Users/Adam/Documents/PACE/TEST"] #hard coded just for presentation
        results = list_file_type(base_drives)
        print_list_file_types(results)


if __name__ == '__main__':
    main()

