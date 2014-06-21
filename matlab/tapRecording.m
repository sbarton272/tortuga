%% audio recording and feature extraction
% want to determine frequencies of taps and extract these

TITLE = 'Glass-Tap1';
SAVE = true;

Fs = 44100;
Wsize = 4096;

recObj = audiorecorder(Fs, 16, 1);
disp('Start recording.')
recordblocking(recObj, 10);
disp('Done recoding');
% Store data in double-precision array.
data = getaudiodata(recObj);
h = figure;
spectrogram(data,ones(1, Wsize),0,Wsize, Fs, 'yaxis'); title(TITLE);
if SAVE
    fname = [TITLE,'-spectrum.fig'];
    saveas(h, fname, 'fig');
end

h = figure;
N = length(data);
w = -N/2:(N/2-1);
plot(w,fftshift(abs(fft(data)))); title(TITLE);
if SAVE
    fname = [TITLE,'-fft'];
    saveas(h, fname, 'fig');
end

fname = [TITLE,'-data'];
save(fname,'data');