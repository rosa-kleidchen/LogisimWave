# LogisimWave
wave viewer for logisim logfiles
# What is Logisim?
Logisim is a very popular logic simulator with many public projects.
# Where is the problem with that?
Logisim has no native waveform viewer integrated. That is a problem.
# Solution?
LogisimWave! What does it mean? Ok, Logisim has a logging function located in the simulation menu.
Load a digital design or create a new one.
Select 'Simulate' -> 'Logging...'
A new dialog appears!
Select the 'File' menu tab and create a logging file, please select 'Include Header Line'.
Change to the menu tab 'Selection' and drag and drop all signals you would like to see in the waveform.
Then simulate your design, ideally by 'Ticks Enabled', when you have a clock or something like that.
After simulation, yout just call LogisimWave as follows:

java -jar LogisimWave.jar -m logisim-logfile
  -m: Monitor mode
  -p: PNG file

This will be created two file called 'gnuplot.dat' and 'lw.plt':
  - gnuplot.dat is the new data file for gnuplot
  - lw.plt is the gnuplot script which can be executed by gnuplot itself

Now you should see the waveform of all signals you selected before.

Oh, you need a gnuplot installation!

Have fun and call me, if something going wrong

e.guentzel@gmx.de
