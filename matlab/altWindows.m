% alternative spectrograms
close all

load('Glass-Tap-22kHz-data.mat', 'data');

Fs = 22050;

h = figure;
spectrogram(data,ones(1, 128),0,128, Fs, 'yaxis'); title('128');

h = figure;
spectrogram(data,ones(1, 256),0,256, Fs, 'yaxis'); title('256');


h = figure;
spectrogram(data,ones(1, 1024),0,1024, Fs, 'yaxis'); title('1024');

h = figure;
spectrogram(data,ones(1, 2048),0,1024, Fs, 'yaxis'); title('2048');

h = figure;
spectrogram(data,ones(1, 4096),0,4096, Fs, 'yaxis'); title('4096');

h = figure;
spectrogram(data,ones(1, 8192),0,8192, Fs, 'yaxis'); title('8192');
