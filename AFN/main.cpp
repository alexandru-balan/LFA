#include <iostream>
#include <set>
#include <fstream>

using namespace std;

struct Transitions {
    char currentState;
    char symbol;
    char nextState;
};

class AFN {
private:
    set <char, greater<>> Q;
    set <char, greater<>> E;
    set <char, greater<>> F;
    char q0;
    Transitions* transitions;
public:
    AFN(const string path_to_afn) {
        ifstream f(path_to_afn);

        if (!f) {
            perror("File cannot be accessed!\n");
            return ;
        }

        int nrOfTransitions;
        f>>nrOfTransitions;
        transitions = new Transitions[nrOfTransitions];

        string line;
        getline(f,line);
        getline(f,line);
        q0 = line[0];
        Q.insert(line[0]);
        transitions[0].currentState = line[0];
        E.insert(line[2]);
        transitions[0].symbol = line[2];
        if (line[4] == '@') {
            //Starea e finala
            Q.insert(line[5]);
            F.insert(line[5]);
            transitions[0].nextState = line[5];
        } else {
            Q.insert(line[4]);
            transitions[0].nextState = line[4];
        }

        int i = 1;

        while(!f.eof()) {
            string line;
            getline(f,line);
            Q.insert(line[0]);
            transitions[i].currentState = line[0];
            E.insert(line[2]);
            transitions[i].symbol = line[2];
            if (line[4] == '@') {
                //Starea e finala
                Q.insert(line[5]);
                F.insert(line[5]);
                transitions[i].nextState = line[5];
            } else {
                Q.insert(line[4]);
                transitions[i].nextState = line[4];
            }

            i++;
        }

        f.close();
    }

    friend ostream& operator<<(ostream& out, AFN afn) {
        set <char, greater<>> :: iterator i;
        out<<"Q = { ";
        for (i = afn.Q.begin(); i != afn.Q.end(); ++i) {
            out<<*i<<" ";
        }
        out<<"}\n";
        out<<"E = { ";
        for (i = afn.E.begin(); i != afn.E.end(); ++i) {
            out<<*i<<" ";
        }
        out<<"}\n";

        out<<"q0 = "<<afn.q0<<"\n";

        out<<"F = { ";
        for (i = afn.F.begin(); i != afn.F.end(); ++i) {
            out<<*i<<" ";
        }
        out<<"}\n";

        return out;
    }
};

int main() {

    AFN afn("afn.in");
    cout<<afn;

    return 0;
}