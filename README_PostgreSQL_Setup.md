# Configuração do PostgreSQL para Hermes Comercial PDV

## Pré-requisitos
- PostgreSQL instalado (versão 12 ou superior)
- pgAdmin ou acesso via linha de comando
- Conhecimento básico de SQL

## Passo 1: Instalação do PostgreSQL

### Windows
1. Baixe o instalador do site oficial: https://www.postgresql.org/download/windows/
2. Execute o instalador com as configurações padrão
3. Anote a senha do usuário `postgres` durante a instalação

### Linux (Ubuntu/Debian)
```bash
sudo apt update
sudo apt install postgresql postgresql-contrib
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### macOS
```bash
brew install postgresql
brew services start postgresql
```

## Passo 2: Criar o Banco de Dados

### Via pgAdmin
1. Conecte-se ao servidor PostgreSQL
2. Clique com o botão direito em "Databases"
3. Selecione "Create" > "Database"
4. Nome: `hermescomercialdb`
5. Owner: `postgres`
6. Clique em "Save"

### Via Linha de Comando
```bash
# Conectar ao PostgreSQL
psql -U postgres

# Criar banco de dados
CREATE DATABASE hermescomercialdb;

# Sair do PostgreSQL
\q
```

## Passo 3: Executar Script de Criação das Tabelas

### Via pgAdmin
1. Conecte-se ao banco `hermescomercialdb`
2. Clique com o botão direito no banco
3. Selecione "Query Tool"
4. Copie e cole o conteúdo do arquivo `database_setup.sql`
5. Clique em "Execute" (F5)

### Via Linha de Comando
```bash
# Conectar ao banco de dados
psql -U postgres -d hermescomercialdb

# Executar script
\i database_setup.sql

# Sair
\q
```

## Passo 4: Verificar Configuração

O arquivo `config.properties` já está configurado para PostgreSQL:

```properties
# PostgreSQL
URL_POSTGRES=jdbc:postgresql://localhost:5432/hermescomercialdb
USER_POSTGRES=postgres
PASSWORD_POSTGRES=123456
```

**IMPORTANTE:** Se você usou uma senha diferente durante a instalação, atualize o campo `PASSWORD_POSTGRES` no arquivo `config.properties`.

## Passo 5: Testar Conexão

Execute a aplicação:

```bash
mvn clean compile exec:java -Dexec.mainClass="com.br.hermescomercialnetbeans.PDVPrincipal"
```

Se tudo estiver correto, a aplicação iniciará sem erros de conexão.

## Estrutura das Tabelas

### Usuários
- Suporta 3 tipos: FUNCIONÁRIO, CLIENTE, FORNECEDOR
- Campos específicos para cada tipo
- Controle de permissões

### Produtos
- Controle completo de estoque
- Preços de venda e compra
- Categorias e subcategorias

### Vendas
- Sistema completo de PDV
- Controle de itens vendidos
- Múltiplas formas de pagamento

### Movimento de Caixa
- Abertura, fechamento, sangria, suprimento
- Controle de saldos
- Auditoria completa

## Dados Iniciais

O script cria automaticamente:
- 2 usuários de exemplo:
  - `admin` / `admin123` (Administrador)
  - `caixa` / `caixa123` (Operador de Caixa)
- 10 produtos de exemplo para testes

## Troubleshooting

### Erro: "Connection refused"
- Verifique se o PostgreSQL está em execução
- Confirme a porta (padrão: 5432)

### Erro: "FATAL: password authentication failed"
- Verifique a senha no `config.properties`
- Confirme o usuário (`postgres` ou outro)

### Erro: "FATAL: database does not exist"
- Execute o script de criação do banco
- Verifique o nome do banco em `config.properties`

### Erro: "relation does not exist"
- Execute o script `database_setup.sql`
- Verifique se você está conectado ao banco correto

## Comandos Úteis

### Verificar status do PostgreSQL
```bash
# Windows
net start postgresql-x64-14

# Linux
sudo systemctl status postgresql

# macOS
brew services list | grep postgresql
```

### Conectar ao banco
```bash
psql -U postgres -d hermescomercialdb
```

### Listar tabelas
```sql
\dt
```

### Verificar estrutura de uma tabela
```sql
\d usuarios
```

### Consultar usuários
```sql
SELECT id, nome, login, tipo_usuario FROM usuarios;
```

## Backup e Restauração

### Backup
```bash
pg_dump -U postgres -d hermescomercialdb > backup.sql
```

### Restauração
```bash
psql -U postgres -d hermescomercialdb < backup.sql
```

---

## Suporte

Caso tenha problemas:
1. Verifique os logs da aplicação na pasta `logs/`
2. Confirme as configurações no `config.properties`
3. Teste a conexão com o PostgreSQL manualmente

O sistema está pronto para uso com PostgreSQL!
