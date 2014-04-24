function [Z] = dtat(Xnew,R,SIZE,C,alpha)   
M=R(1);
Q=R(2);
L=R(3);
T1 = tensor(Xnew,[M,Q,L]);
N = ndims(T1); 
%% Update covariance matrices C and compute U
U = {};
XM = [];
for n = 1:N
    switch class(T1)
        case {'tensor'} %dense
            XM = double(tenmat(T1,n));
        case {'sptensor'} %sparse
            XM = double(sptenmat(T1,n));
    end
    Cnew{n} = alpha*C{n} + XM*XM';
    opts.disp = 0;
    opts.issym = 1;
    [U{n}, D] = eigs(Cnew{n},SIZE(n),'LM',opts);    
end

%% compute core
core = ttm(T1, U, 't');
T = ttensor(core, U);
T = full(T);
Z = zeros(R(1),R(2),R(3));
for i=1:L
    Z(:,:,i)=T(:,:,i);
end