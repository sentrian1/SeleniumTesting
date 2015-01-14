package mainPackage;

public class NewArray {
	public Object[] array = {};
	
	public NewArray(Object[] a){
		array = a;
	}
	public NewArray(){
		super();
	}
	public NewArray(String... args){
		//System.out.println(args.length);
		for(String s: args){
			this.add(s);
		}
	}
	public NewArray(int... args){
		for(int n: args){
			this.add(n);
		}
	}
	
	public void add(Object... n){
		for(Object o: n){
			this.add(o);
		}
	}
	public void add(Object n){
		Object[] newarray;
		int len = array.length;
		newarray = new Object[len+1];
		for(int i=0;i<len;i++){
			newarray[i] = array[i];
		}
		newarray[len] = n;
		array = newarray;
	}
	
	public void display(){
		String str = "{";
		for(int i=0;i<array.length;i++){
			if(i!=array.length-1){
				if(array[i] instanceof String){
					str = str + "'"+array[i].toString()+"'"+",";
				}
				else{
					str = str + array[i].toString()+",";
				}
			}
			else{
				if(array[i] instanceof String){
					str = str + "'" + array[i].toString() + "'";
				}else{
					str = str + array[i].toString();
				}
			}
		}
		str = str + "}";
		System.out.println(str);
	}
	
	public void remove(Object n){
		NewArray newarray = new NewArray();
		int len = array.length;

		for(int i=0;i<len;i++){
			if(array[i].equals(n)){
				continue;
			}
			newarray.add(array[i]);
		}
		array = newarray.array;
	}
	
}


