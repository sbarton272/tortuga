% feature extraction
close all

load('Glass-Tap-44.1kHz-4096-data.mat', 'data');
Fs = 44100;
Wsize = 4096;

figure;
subplot(121);
spectrogram(data,ones(1, Wsize),0,Wsize, Fs, 'yaxis'); title('raw')
[S, F, T, P] = spectrogram(data,ones(1, Wsize),0,Wsize, Fs, 'yaxis'); title('raw');
subplot(122);
N = length(data);
w = -N/2:(N/2-1);
plot(w,fftshift(abs(fft(data)))); title('raw')

% Features extracted from spectral power density

Samples = size(P,2);
nFreqs = size(P,1);
FrequenciesToSample = 100; % get bottom 100 samples


Avgs = zeros(1,Samples);
StdDevs = zeros(1,Samples);
TotalPower = zeros(1,Samples);
MaxPower = zeros(1,Samples);
MaxPowerFreq = zeros(1,Samples);
FreqSamples = zeros(FrequenciesToSample, Samples);

for i = 1:Samples
    x = P(:,i);
    
    Avgs(i) = mean(x);
    StdDevs(i) = std(x);
    TotalPower(i) = sum(x);
    [MaxPower(i), ind] = max(x);
    MaxPowerFreq(i) = F(ind); 
    FreqSamples(:,i) = x(1:FrequenciesToSample);
    
    % visualize
    plot(FreqSamples(:,i));
    disp(T(i));
    disp(MaxPower(i));
    disp(MaxPowerFreq(i));
    disp('---');
    pause
end
    


