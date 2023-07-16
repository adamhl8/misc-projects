#!/usr/bin/env python

import subprocess
import sys
from pathlib import Path


def handle_files(directory):
    video_extensions = [".mp4", ".m4v", ".mkv", ".flv", ".avi", ".mov", ".wmv"]

    for file_path in Path(directory).rglob('*'):
        if any(file_path.suffix.lower() == ext for ext in video_extensions):
            print(f"\n{file_path}")

            out_file_path = file_path.with_name(f'{file_path.stem}_out.mp4')

            command = [
              'ffmpeg',
              '-i',
              f'{file_path}',
              '-c:v',
              'libx265',
              '-preset',
              'faster',
              '-crf',
              '26',
              '-c:a',
              'aac',
              '-b:a',
              '128k',
              '-movflags',
              '+faststart',
              '-sn',
              '-map_metadata',
              '-1',
              '-map_chapters',
              '-1',
              f'{out_file_path}'
            ]

            subprocess.run(command)

            file_path.unlink()
            out_file_path.rename(file_path.with_name(f'{file_path.stem}.mp4'))

if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("Usage: python script.py [directory]")
        sys.exit(1)

    directory = sys.argv[1]
    handle_files(directory)
