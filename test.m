
M = load('url.mat', 'Day0');
fields = fieldnames(M);
Day = M.(fields{1});
dayFields = fieldnames(Day);

X = Day.(dayFields{1});
Y = Day.(dayFields{2});

perceptron.update = 'perceptron';
pa.update = 'pa';

errs = [];
mems = [];
times = [];
runs = 2;


    start = cputime;
    Xrow = transpose(X);
    Yrow = Y;
    [err, mu, sigma, mem] = cw(Xrow,Yrow,perceptron);
    time = cputime - start;
    if i == 1
      errs = err;
      mems = mem;
      times = time;
    else
      errs(:, i) = err;
      mems(i) = mem;
      times(i) = time;
    end
    disp(sprintf('Processing instance %d...',i));

format = 'bx--';
name = 'perceptron';
total_err = mean(errs, 2);
plot(total_err,format);

%disp(net.IW{1})
%disp(net.b{1});

