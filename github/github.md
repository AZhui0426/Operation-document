# github通过SSH验证拉取、提交代码

```
# git bash
# check user.name & user.email
git config --global --list

# change name&email
git config --global  user.name "your name"
git config --global user.email "your email"

# generate ssh key
ssh-keygen -t rsa -C "your email"

# 1.确认秘钥的保存路径（如果不需要改路径则直接回车）；
# 2.如果上一步置顶的保存路径下已经有秘钥文件，则需要确认是否覆盖（如果之前的秘钥不再需要则直接回车覆盖，如需要则手动拷贝到其他目录后再覆盖）；
# 3.创建密码（如果不需要密码则直接回车）；
# 4.确认密码；

# github.com
# user --> settings --> SSH and GPG keys --> New SSH key
# copy id_rsa.pub

# test
ssh -T git@github.com
```

PS：注意git clone选择SSH，否则无效。



# sourceTree使用注意事项

```
# 工具 --> 选项 --> 一般 --> SSH客户端配置 --> SSH客户端选择OpenSSH
```

