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
    set <char, greater<>> nextStates;
    char q0;
    int nrOfTransitions;
    Transitions* transitions;

    int delta (char symbol) {
        set <char, greater<>> replaceStates;
        for (int i = 0; i < nrOfTransitions; ++i) {
            if (nextStates.count(transitions[i].currentState) && (symbol == transitions[i].symbol)) {
                replaceStates.insert(transitions[i].nextState);
            }
        }
        if(replaceStates.empty()) {
            return 0; // The automaton does not accept the word because there are not enough transitions
        }
        nextStates = replaceStates;
        return 1;
    }

public:
    AFN(const string path_to_afn) {
        ifstream f(path_to_afn);

        if (!f) {
            perror("File cannot be accessed!\n");
            return ;
        }

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

        nextStates.insert(q0);

        f.close();
    }

    void accept (string word) {
        for (int i = 0; i < word.length(); ++i) {
            if(!delta(word[i])) {
                cout<<"Word is not accepted\n";
                return;
            }
        }

        set<char, greater<>>::iterator i;

        for(i = nextStates.begin(); i != nextStates.end(); ++i) {
            if(F.count(*i)) {
                cout<< "Word is accepted\n";
                return;
            }
        }

        cout<<"Word is not accepted\n";
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

    string word = "ababbaab";
    cout<<"word = "<<word<<endl;
    afn.accept(word);

    return 0;
}