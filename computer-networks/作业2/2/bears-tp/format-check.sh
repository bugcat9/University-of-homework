#!/bin/bash

tarfile=$1

if [ ! -f "$tarfile" ]; then
  echo "Usage: $0 <tar file>"
  exit 1
fi

if [[ $(basename $PWD) != "bears-tp" ]]; then
  echo "Must run from bears-tp directory."
  exit 1
fi

fail()
{
  echo "---------------------------------------------------------------"
  echo "Improperly formatted tar file!"
  echo "The tar file should contain Sender.py and a README.txt file"
  echo "Example:"
  echo " $ cd bears-tp"
  echo " $ tar -cf project2-obama-biden.tar Sender.py README.txt"
  echo "---------------------------------------------------------------"
  if [ -f FormatCheckSender.py ]; then
    rm FormatCheckSender.py
  fi
  exit 1
}

# Check if files exist
FAIL=0
mkdir -p temp-project-directory
cd temp-project-directory
tar -xf ../"$tarfile"
cd ..
if [ ! -f temp-project-directory/Sender.py ]; then
  echo "Sender.py not found!"
  FAIL=1
else
  echo "Found Sender.py"
  cp temp-project-directory/Sender.py FormatCheckSender.py
fi
if [ ! -f temp-project-directory/README.txt ]; then
  echo "README.txt not found!"
  FAIL=1
else
  echo "Found README.txt"
fi
rm -rf temp-project-directory
if [ $FAIL == 1 ]; then
  fail
fi

# Run the simple tests for sanity check
python TestHarness.py -s FormatCheckSender.py -r Receiver.py
rm FormatCheckSender.py
