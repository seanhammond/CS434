function test()
M = load('url.mat', 'Day0');
fields = fieldnames(M);
Day = M.(fields{1});
dayFields = fieldnames(Day);

X = Day.(dayFields{1});
Y = Day.(dayFields{2});

perceptron.update = 'perceptron';
pa.update = 'pa';
diag.sparsity = 'diag_l2';
buf(1).FAm = 2;
buf(2).FAm = 4;
buf(3).FAm = 8;
buf(4).FAm = 16;
buf(5).FAm = 32;
buf(6).FAm = 64;
[buf(:).sparsity] = deal('buffer');
fa(1).FAm = 2;
fa(2).FAm = 4;
fa(3).FAm = 8;
fa(4).FAm = 16;
fa(5).FAm = 32;
fa(6).FAm = 64;
[fa(:).sparsity] = deal('FAinv');
colors = 'mrygbc';
full.sparsity = 'full';

Xrow = transpose(X);
Yrow = Y;

disp(sprintf('size(xrow) %d...',size(Xrow)));

figure
graph(X,Y,perceptron,'bx--', 'perceptron');
graph(X,Y,pa,'rx--', 'pa');
graph(X,Y,diag,'k', 'cw-diag');
for i = 1:length(fa)
  graph(X,Y,fa(i),colors(i), sprintf('cw-fa%d', fa(i).FAm));
end
graph(X,Y,full,'k--', 'cw-full');

%disp(net.IW{1})
%disp(net.b{1});

function graph(Xrow,Yrow,params,format,name)
  params
  runs = 2
  errs = [];
  mems = [];
  times = [];
  for i = 1:runs
    start = cputime;
    [err,mu,sigma,mem] = cw(Xrow,Yrow,params);
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
  end
  total_err = mean(errs, 2);
  plot(total_err,format);

  save(sprintf('synth_results_%s.mat', name), 'errs', 'mems', 'times', 'format');
  hold on; drawnow

