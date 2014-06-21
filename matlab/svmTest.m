%This code was written by Cody Neuburger cneuburg@fau.edu
%Florida Atlantic University, Florida USA
%This code was adapted and cleaned from Anand Mishra's multisvm function
%found at http://www.mathworks.com/matlabcentral/fileexchange/33170-multi-class-support-vector-machine/

% trying out an SVM
clear all
close all

load('features-3.mat', 'Features');

GroupTrain = Features{2};
TrainingSet = Features{1};

load('testSet-3.mat', 'Features');

TestSet = Features{1};

uniqueGroups = unique(GroupTrain);
numClasses = length(uniqueGroups); 
result = cell(length(TestSet(:,1)),1);

%build models 
for k = 1:numClasses 
    %Vectorized statement that binarizes Group 
    %where 1 is the current class and 0 is all other classes 
    G1vAll = cellfun(@(x) strcmp(x,uniqueGroups{k}),GroupTrain); 
    models(k) = svmtrain(TrainingSet,G1vAll,'kernel_function','rbf'); 
end

%classify test cases 
for j=1:size(TestSet,1) % go elem by elem
    for k=1:numClasses % try all models
        if(svmclassify(models(k),TestSet(j,:))) 
            break; 
        end 
    end 
    result{j} = uniqueGroups{k}; 
end

comp = {result, Features{2}};
for i = 1:size(comp,1)
    x = comp{1,:};
    disp(x);
end