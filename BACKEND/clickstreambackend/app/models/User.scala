package models

case class User(name:String,email:String,password:String){
    override def toString = s"Name : $name\nemail : $email"
}