extern int __VERIFIER_nondet_int();

void main()
{
	int old, new;
    bool lock = 0;
	
	do {
		assert(!lock);
		lock = 1;
		old = new;
	if (__VERIFIER_nondet_int()) {
		lock = 0;
			new++;
		}
	} while (new != old);
    
}
