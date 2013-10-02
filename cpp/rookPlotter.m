%The Rooks Walk

%Easy      : 8  steps, 0 hints
%Medium    : 12 steps, 1 hint
%Hard      : 16 steps, 3 hints
%Hardest   : 20 steps, 4 hints

function rookPlotter()

%Seed the random number generator!  If you don't do this, you get the same
%puzzle every time.  Oops.

RandStream.setDefaultStream(RandStream('mt19937ar','seed',sum(100*clock)));

lenv = [10,10,10,10];
hv   = [4 4 4 4];

for i = 3:4
    h   = hv(i);
    
    if (i < 3)
        system(['./test 6 6 ', num2str(lenv(i))]);
    else
        system(['./test 6 6 ', num2str(lenv(i))]);
    end
    
    [S,P] = boardRead('iotest.txt');
    P = P + 1;
    boardplot(S,P,h);

    switch i
        case 1
            print -dpng easy.png
        case 2
            print -dpng medium.png
        case 3
            print -dpng harder.png
        case 4
            print -dpng hardest.png
    end

    rooksolnplot(S,P)

    switch i
        case 1
            print -dpng easysoln.png
        case 2
            print -dpng mediumsoln.png
        case 3
            print -dpng hardersoln.png
        case 4
            print -dpng hardestsoln.png
    end

end


end

function [S,P] = boardRead(filename)
    %Read File created by LJJ_Rook.cpp
    file = load(filename);
    N = file(1);
    M = file(2);
    S = zeros(N,M);
    c = 3;
    for i = 1:N
        for j = 1:M
            S(i,j) = file(c);
            c = c+1;
        end
    end
    pnum = file(c);
    P = zeros(pnum,2);
    c = c+1;
    for i = 1:pnum
        P(i,1) = file(c);
        c = c+1; 
        P(i,2) = file(c);
        c = c+1;
    end
end

function boardplot(S,P,h)
    D = (S ~= -1);
    S = S + (1-D);
    
    [N,M]=size(S);
    [X,Y]=ndgrid(1:N,1:M);
    
    %Draw the board grid
    x1=[0,N];y1=[0 0];sqx = [0 1 1 0 0];sqy = [0 0 1 1 0];

    clf
    axis off
    hold on
    axis square
    whitebg
    axis([0, N+.5, -N-.5, 0]);
%     fill([-.5 N+1.5 N+1.5 -.5 -.5],[.5 .5 -N-1.5 -N-1.5 .5],'w')

    for i = 1:N+1;
        plot(x1+.5,y1+.5-i,'k','linewidth',4)
        plot(y1-.5+i,-.5-x1,'k','linewidth',4)
    end

    vec = find(D==0);
    for i = 1:length(vec)
        fill(Y(vec(i))+sqx-.5,-X(vec(i))+sqy-.5,'k')
    end
    
    v1 = sum(S);
    v2 = sum(S');
    
%    for i = 1:N
%        text(i,0,num2str(v1(i)),'FontSize',14,'HorizontalAlignment','center')
%        text(0,-i,num2str(v2(i)),'FontSize',14,'HorizontalAlignment','center')
%    end
       
    br = 0;
    while br == 0 
        v1 = randint(1,h,[2 length(P(:,1))]);
        br = (length(v1)==length(unique(v1)));
    end
    
    for i = 1:length(v1)
        text(P(v1(i),2),-P(v1(i),1),num2str(S(P(v1(i),1),P(v1(i),2))),'FontSize',14,'HorizontalAlignment','center')
    end

end

function rooksolnplot(S,P)
    s = size(S);
    p = size(P);
    rowmov = zeros(1,s(1));
    colmov = zeros(1,s(2));
    figure(1)
    solnboardplot(S,P)
    hold on
    for i = 1:p(1)-1
        if P(i,1)==P(i+1,1)
            rowmov(P(i,1))=rowmov(P(i,1))+1;
            linklinexy(P(i,:),P(i+1,:),rowmov(P(i,1)),2);
        elseif P(i,2)==P(i+1,2)
            colmov(P(i,2))=colmov(P(i,2))+1;
            linklinexy(P(i,:),P(i+1,:),colmov(P(i,2)),1);
        end
    end 
    
    x = 0:.05:2*pi;
    xc = .15*cos(x);
    yc = .15*sin(x);
    
    for i = 1:length(P(:,1))
        text(P(i,2),-P(i,1),num2str(S(P(i,1),P(i,2))),'FontSize',14,'HorizontalAlignment','center')
    end   
   
end

function out = linklinexy(sta,fin,num,xy)
    str = abs(sta(xy)-fin(xy));
    cut = .07;
    
    rez = cut/(str^.5):.01:1-cut/(str^.5);
    x = rez*str+min([sta(xy),fin(xy)]);
    
    switch num
        case 1
            y = .4*sin(rez*pi).^.5-sta(3-xy);
        case 2
            y = -.4*sin(rez*pi).^.5-sta(3-xy);
        case 3
            y = .23*sin(rez*pi).^.5-sta(3-xy);
        case 4
            y = -.23*sin(rez*pi).^.5-sta(3-xy);
        case 5
            y = 0*sin(rez*pi).^.5-sta(3-xy);
    end
    if xy == 2        
        plot(x,y,'k')
    else
        plot(-y,-x,'k')
    end
end

function solnboardplot(S,P)
    
    %Get Dead Space and dimensions
    D = (S ~= -1);
    S = S + (1-D);
    [N,M]=size(S);
    [X,Y]=ndgrid(1:N,1:M);
    
    %Define coordinates for squares, diamonds and circles
    x1=[0,N];y1=[0 0];sqx = [0 1 1 0 0];sqy = [0 0 1 1 0];
    
    %Initilize and draw background
    clf
    axis off
    axis square
    hold on
    whitebg
    axis([0, N+.5, -N-.5, 0]);
%     fill([-.5 N+1.5 N+1.5 -.5 -.5],[.5 .5 -N-1.5 -N-1.5 .5],'w')
    
   

    %Plot the grid
    for i = 1:N+1;
        plot(x1+.5,y1+.5-i,'k','linewidth',4)
        plot(y1-.5+i,-.5-x1,'k','linewidth',4)
    end

    %Find dead sqaures and plot them
    vec = find(D==0);
    for i = 1:length(vec)
        fill(Y(vec(i))+sqx-.5,-X(vec(i))+sqy-.5,'k')
    end
    
    %Find row and col sums and plot them
    v1 = sum(S);
    v2 = sum(S');
    
    for i = 1:N
        text(i,0,num2str(v1(i)),'FontSize',14,'HorizontalAlignment','center')
        text(0,-i,num2str(v2(i)),'FontSize',14,'HorizontalAlignment','center')
    end
   
    %Plot the start numbers
    for i = 1:length(P(:,1))
        text(P(i,2),-P(i,1),num2str(S(P(i,1),P(i,2))),'FontSize',14,'HorizontalAlignment','center')
    end   
    axis square
end

function out = randint(n,m,lims)
    out = lims(1)+floor((lims(2)-lims(1)+1)*rand(n,m));
end
