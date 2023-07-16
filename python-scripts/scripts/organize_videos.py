#!/usr/bin/env python

import subprocess
import sys
from pathlib import Path


def handle_files(directory):
    video_extensions = [".mp4", ".m4v", ".mkv", ".flv", ".avi", ".mov", ".wmv"]

    # walk through the directory
    for file_path in Path(directory).rglob('*'):
        # check if the file is a video file
        if any(file_path.suffix.lower() == ext for ext in video_extensions):

            # open the file with VLC
            player = subprocess.Popen(['vlc', str(file_path)], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

            print(f"\n{file_path.name}")
            # ask for input
            action = input("Enter new filename to rename, 'd' to delete, or 's' to skip: ")

            player.terminate()
            while player.poll() is None:
                pass

            # delete the file if the user enters 'd'
            if action.lower() == 'd':
                file_path.unlink()
            elif action.lower() == 's':
                continue
            else:
                new_file_path = file_path.with_stem(action) # changing file stem keeps the original suffix
                counter = 2
                while new_file_path.exists():
                    new_file_path = file_path.with_stem(f"{action}_{counter}")
                    counter += 1
                file_path.rename(new_file_path)

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python script.py [directory]")
        sys.exit(1)

    directory = sys.argv[1]
    handle_files(directory)
